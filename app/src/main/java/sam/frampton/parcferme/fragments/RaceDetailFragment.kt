package sam.frampton.parcferme.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import sam.frampton.parcferme.R
import sam.frampton.parcferme.adapters.QualifyingResultAdapter
import sam.frampton.parcferme.adapters.RaceResultAdapter
import sam.frampton.parcferme.data.Race
import sam.frampton.parcferme.databinding.FragmentRaceDetailBinding
import sam.frampton.parcferme.viewmodels.MainActivityViewModel
import sam.frampton.parcferme.viewmodels.RaceDetailViewModel
import sam.frampton.parcferme.workers.RaceReminderWorker
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.TimeUnit

class RaceDetailFragment : Fragment() {

    private val raceDetailViewModel: RaceDetailViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val args: RaceDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentRaceDetailBinding
    private lateinit var raceResultAdapter: RaceResultAdapter
    private lateinit var qualifyingResultAdapter: QualifyingResultAdapter
    private lateinit var race: Race
    private var reminder: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRaceDetailBinding.inflate(layoutInflater)
        setRace()
        setupRecyclerView()
        setupChips()
        setupObservers()
        return binding.root
    }

    private fun setRace() {
        race = args.race
        binding.race = race
        raceDetailViewModel.setRace(race)
        refresh(false)
    }

    private fun setupRecyclerView() {
        raceResultAdapter = RaceResultAdapter { result ->
            val action = RaceDetailFragmentDirections
                .actionRaceDetailFragmentToDriverDetailFragment(
                    result.driver,
                    result.driver.getTitle(requireContext())
                )
            findNavController().navigate(action)
        }
        qualifyingResultAdapter = QualifyingResultAdapter { result ->
            val action = RaceDetailFragmentDirections
                .actionRaceDetailFragmentToDriverDetailFragment(
                    result.driver,
                    result.driver.getTitle(requireContext())
                )
            findNavController().navigate(action)
        }
        binding.rvRaceDetail.adapter = raceResultAdapter
        binding.rvRaceDetail.setOrientedLayoutManager()
        binding.swipeRefreshRaceDetail.setOnRefreshListener { refresh(true) }
        binding.swipeRefreshRaceDetail.setColor()
    }

    private fun setupChips() {
        binding.chipGroupRaceDetail.setOnCheckedChangeListener { _, checkedId ->
            binding.rvRaceDetail.adapter =
                when (checkedId) {
                    R.id.chip_race_detail_race -> raceResultAdapter
                    R.id.chip_race_detail_qualifying -> qualifyingResultAdapter
                    else -> raceResultAdapter
                }
        }
    }

    private fun setupObservers() {
        raceDetailViewModel.raceResultList.observe(
            viewLifecycleOwner,
            raceResultAdapter::submitList
        )
        raceDetailViewModel.raceResultRefreshResult.observe(viewLifecycleOwner) { refreshResult ->
            refreshResult?.let {
                mainActivityViewModel.setRefreshResult(refreshResult)
                raceDetailViewModel.clearRaceResultRefreshResult()
            }
        }
        raceDetailViewModel.raceResultIsRefreshing.observe(viewLifecycleOwner) { isRefreshing() }

        raceDetailViewModel.qualifyingResultList.observe(
            viewLifecycleOwner,
            qualifyingResultAdapter::submitList
        )
        raceDetailViewModel.qualifyingResultRefreshResult
            .observe(viewLifecycleOwner) { refreshResult ->
                refreshResult?.let {
                    mainActivityViewModel.setRefreshResult(refreshResult)
                    raceDetailViewModel.clearQualifyingResultRefreshResult()
                }
            }
        raceDetailViewModel.qualifyingResultIsRefreshing.observe(viewLifecycleOwner) {
            isRefreshing()
        }

        mainActivityViewModel.menuRefresh.observe(viewLifecycleOwner) { refresh ->
            if (refresh == true) {
                refresh(true)
                mainActivityViewModel.clearMenuRefresh()
            }
        }

        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData("${race.season}${race.round}")
            .observe(viewLifecycleOwner) { workInfo ->
                val isEnqueued = workInfo.any { it.state == WorkInfo.State.ENQUEUED }
                reminder?.let {
                    val text = if (isEnqueued) {
                        getString(R.string.race_reminder_on)
                    } else {
                        getString(R.string.race_reminder_off)
                    }
                    Snackbar.make(binding.root, text, BaseTransientBottomBar.LENGTH_SHORT)
                        .apply {
                            anchorView = requireActivity().findViewById(R.id.bottom_navigation)
                        }
                        .show()
                }
                reminder = isEnqueued
                requireActivity().invalidateOptionsMenu()
            }
    }

    private fun refresh(force: Boolean) {
        raceDetailViewModel.refreshRaceResults(force)
        raceDetailViewModel.refreshQualifyingResults(force)
    }

    private fun isRefreshing() {
        binding.swipeRefreshRaceDetail.isRefreshing =
            raceDetailViewModel.raceResultIsRefreshing.value == true
                    || raceDetailViewModel.qualifyingResultIsRefreshing.value == true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (race.time != null && millisUntilRace() > 0) {
            reminder?.let { reminder ->
                inflater.inflate(R.menu.race_detail_menu, menu)
                menu.findItem(R.id.menu_race_reminder_off).isVisible = !reminder
                menu.findItem(R.id.menu_race_reminder_on).isVisible = reminder
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_race_reminder_off -> {
                addRaceReminder()
                return true
            }
            R.id.menu_race_reminder_on -> {
                WorkManager.getInstance(requireContext())
                    .cancelUniqueWork("${race.season}${race.round}")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addRaceReminder() {
        val time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(race.time)
        val data = workDataOf(
            RaceReminderWorker.KEY_RACE_SEASON to race.season,
            RaceReminderWorker.KEY_RACE_NAME to race.raceName,
            RaceReminderWorker.KEY_RACE_TIME to time
        )
        val workRequest = OneTimeWorkRequestBuilder<RaceReminderWorker>()
            .setInputData(data)
            .setInitialDelay(millisUntilRace(), TimeUnit.MILLISECONDS)
            .addTag("${race.season}${race.round}")
            .build()
        WorkManager.getInstance(requireContext()).enqueueUniqueWork(
            "${race.season}${race.round}",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun millisUntilRace(): Long {
        val raceDateTime = ZonedDateTime.of(race.date, race.time, ZoneOffset.UTC)
        return Duration.between(Instant.now(), raceDateTime).toMillis()
    }
}