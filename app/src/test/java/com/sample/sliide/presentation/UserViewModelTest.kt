package com.sample.sliide.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sample.sliide.domain.User
import com.sample.sliide.domain.UserUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class UserViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @SpyK
    var useCase: UserUseCase = UserUseCase()

    @MockK
    private lateinit var unitObserver: Observer<in Unit>

    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = UserViewModel(useCase)
        every { useCase.users() } returns Single.just(listOf(testUser))
    }

    @Test
    fun `Users are loaded after ViewModel is created`() {
        verify { useCase.users() }
    }

    @Test
    fun `Dialog is displayed on add user button click`() {
        viewModel.onAddUserClick()
        viewModel.addUserDialog.observeForever(unitObserver)

        verify { unitObserver.onChanged(Unit) }
    }

    @Test
    fun `User is added after confirmation`() {
        every { useCase.addUser(testUser.name, testUser.email) } returns Single.just(testUser)
        viewModel.onAddUserDialogClicked(testUser.name, testUser.email)

        verify { useCase.addUser(testUser.name, testUser.email) }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }

    companion object {
        private val testUser =
            User(id = 1, name = "John", email = "john.doe@gmail.com", createdHoursAgo = 4)
    }
}