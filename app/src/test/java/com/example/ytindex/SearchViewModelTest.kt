package com.example.ytindex

import com.example.ytindex.data.VideoResult
import com.example.ytindex.data.repository.BookmarkRepository
import com.example.ytindex.data.repository.QuotaExceededException
import com.example.ytindex.data.repository.SearchRepository
import com.example.ytindex.ui.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val searchRepository = mockk<SearchRepository>()
    private val bookmarkRepository = mockk<BookmarkRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { bookmarkRepository.getBookmarkedVideoIds() } returns setOf("bookmarked-1")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testOnQueryChangeAndClampedLimit() = runTest {
        val vm = SearchViewModel(searchRepository, bookmarkRepository, null)
        advanceUntilIdle()

        vm.onQueryChange("compose")
        assertEquals("compose", vm.state.value.query)

        vm.onLimitChange(100)
        assertEquals(50, vm.state.value.limit)

        vm.onLimitChange(-5)
        assertEquals(1, vm.state.value.limit)
    }

    @Test
    fun testSuccessfulSearch() = runTest {
        val sampleVideos = listOf(
            VideoResult(videoId = "1", title = "V1", channel = "C1"),
            VideoResult(videoId = "2", title = "V2", channel = "C2")
        )
        coEvery { searchRepository.searchVideos("kotlin", 10) } returns sampleVideos

        val vm = SearchViewModel(searchRepository, bookmarkRepository, null)
        advanceUntilIdle()

        vm.onQueryChange("kotlin")
        vm.search()
        advanceUntilIdle()

        assertFalse(vm.state.value.loading)
        assertEquals(2, vm.state.value.results.size)
        assertTrue(vm.state.value.hasSearched)
    }

    @Test
    fun testQuotaExceededErrorMapping() = runTest {
        coEvery { searchRepository.searchVideos("youtube", 10) } throws QuotaExceededException(
            "Batas kuota harian YouTube Data API telah tercapai (quotaExceeded). Silakan coba lagi besok."
        )

        val vm = SearchViewModel(searchRepository, bookmarkRepository, null)
        advanceUntilIdle()

        vm.onQueryChange("youtube")
        vm.search()
        advanceUntilIdle()

        assertFalse(vm.state.value.loading)
        assertTrue(vm.state.value.results.isEmpty())
        assertTrue(vm.state.value.error?.contains("quotaExceeded") == true)
    }
}
