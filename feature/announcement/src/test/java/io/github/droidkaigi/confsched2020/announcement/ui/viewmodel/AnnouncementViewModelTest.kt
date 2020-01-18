package io.github.droidkaigi.confsched2020.announcement.ui.viewmodel

import com.jraska.livedata.test
import io.github.droidkaigi.confsched2020.announcement.ui.item.AnnouncementItem
import io.github.droidkaigi.confsched2020.model.repository.AnnouncementRepository
import io.github.droidkaigi.confsched2020.widget.component.MockkRule
import io.github.droidkaigi.confsched2020.widget.component.ViewModelTestRule
import io.kotlintest.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class AnnouncementViewModelTest {
    @get:Rule
    val viewModelTestRule = ViewModelTestRule()
    @get:Rule
    val mockkRule = MockkRule(this)
    @MockK(relaxed = true)
    lateinit var announcementRepository: AnnouncementRepository
    @MockK(relaxed = true)
    lateinit var announcementItemFactory: AnnouncementItem.Factory

    @Test
    fun load() {
        coEvery { announcementRepository.announcements() } returns flowOf(Dummies.announcements)
        val announcementViewModel =
            AnnouncementViewModel(announcementRepository, announcementItemFactory)

        val testObserver = announcementViewModel
            .uiModel
            .test()

        val valueHistory = testObserver.valueHistory()
        valueHistory[0] shouldBe AnnouncementViewModel.UiModel.EMPTY.copy(isLoading = true)
        valueHistory[1].apply {
            isLoading shouldBe false
            announcementItems shouldBe Dummies.announcements.map { announcementItemFactory.create(it) }
            error shouldBe null
            isEmpty shouldBe false
        }
    }
}