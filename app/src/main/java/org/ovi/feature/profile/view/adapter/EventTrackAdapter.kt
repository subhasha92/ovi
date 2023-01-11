package org.ovi.feature.profile.view.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ovi.R
import org.ovi.databinding.ItemEventListTrackerBinding
import org.ovi.feature.events.model.EventDetailsMode
import org.ovi.feature.home.model.RowsItem
import org.ovi.feature.survey.view.activity.SurveyActivity
import org.ovi.feature.survey.view.activity.SurveyType
import org.ovi.util.extensions.*
import kotlin.reflect.KFunction2

class EventTrackAdapter(
    private val rowItems: MutableList<RowsItem>,
    private val callBack: KFunction2<EventDetailsMode, Int, Unit>
) :
    RecyclerView.Adapter<EventTrackAdapter.VH>() {

    inner class VH(val binding: ItemEventListTrackerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.applyMatchWrapContent()
        }

        @SuppressLint("SetTextI18n")
        fun bind(rowsItem: RowsItem) {
            with(binding) {
                binding.root.setOnClickListener {
                    when (tvAttendStatus.text) {
                        "ATTENDED" -> {
                            callBack.invoke(
                                EventDetailsMode.ATTENDED,
                                rowsItem.id!!
                            )

                        }
                        "REGISTERED" -> {
                            callBack.invoke(
                                EventDetailsMode.CANCEL,
                                rowsItem.id!!
                            )

                        }
                        "INVITED" -> {
                            callBack.invoke(
                                EventDetailsMode.ACCEPT,
                                rowsItem.id!!
                            )

                        }
                        else -> {
                            callBack.invoke(
                                EventDetailsMode.CANCEL,
                                rowsItem.id!!
                            )

                        }
                    }

                }
                tvTitle.text = rowsItem.name
                tvDatenLoc.text = if (rowsItem.event_start_date != null) "${
                    rowsItem.event_start_date.toString().getFormatedDateToDisplay()
                } ${rowsItem.county ?: "N/A"}" else "N/A"

                if (rowsItem.participants?.get(0)?.has_attended == true) {
                    tvAttendStatus.text = "ATTENDED"
                    incSurveyBtn.root.show()
                    checkSurveyAttended(rowsItem)
                } else if (rowsItem.participants?.get(0)?.invited_on == null && rowsItem.participants?.get(
                        0
                    )?.invitation_status == "accepted"
                ) {
                    tvAttendStatus.text = "REGISTERED"
                    incSurveyBtn.root.show()
                    incSurveyBtn.tvTakePost.gone()
                    checkSurveyAttended(rowsItem)

                } else if (rowsItem.participants?.get(0)?.invitation_status == "pending")
                    tvAttendStatus.text = "INVITED"
                else {
                    tvAttendStatus.text =
                        rowsItem.participants?.get(0)?.invitation_status.toString().uppercase()
                    incSurveyBtn.root.show()
                    incSurveyBtn.tvTakePost.gone()
                    checkSurveyAttended(rowsItem)
                }

                if (rowsItem.post_survey_id == null || rowsItem.post_survey_id == "")
                    incSurveyBtn.tvTakePost.gone()
                if (rowsItem.pre_survey_id == null || rowsItem.pre_survey_id == "") {
                    incSurveyBtn.tvTakePre.gone()
                }

                incSurveyBtn.tvTakePost.setOnClickListener {
                    if (rowsItem.post_survey_id != null)
                        SurveyActivity.present(
                            incSurveyBtn.tvTakePost.context,
                            rowsItem.post_survey_id,
                            rowsItem.id,
                            SurveyType.PRE.value
                        )
                }
                incSurveyBtn.tvTakePre.setOnClickListener {
                    if (rowsItem.pre_survey_id != null)
                        SurveyActivity.present(
                            incSurveyBtn.tvTakePost.context,
                            rowsItem.pre_survey_id,
                            rowsItem.id,
                            SurveyType.PRE.value
                        )
                }

            }
        }

        private fun checkSurveyAttended(data: RowsItem) {
            if (data.pre_survey_responses != null) {
                if (data.pre_survey_responses.isNotEmpty())
                    if (binding.incSurveyBtn.tvTakePre.isVisible()) {
                        binding.incSurveyBtn.tvTakePre.hasAttended(binding.incSurveyBtn.tvTextSurveyPre)
                        binding.incSurveyBtn.tvTextSurveyPre.text =
                            binding.root.context.getOviString(R.string.pre_survey_taken)
                    }
            }
            if (data.post_survey_responses != null) {
                if (data.post_survey_responses.isNotEmpty())
                    if (binding.incSurveyBtn.tvTakePost.isVisible()) {
                        binding.incSurveyBtn.tvTakePost.hasAttended(binding.incSurveyBtn.tvTextSurveyPost)
                        binding.incSurveyBtn.tvTextSurveyPost.text =
                            binding.root.context.getOviString(R.string.post_survey_taken)
                    }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemEventListTrackerBinding.inflate(parent.inflater()))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(rowItems[position])
    }

    override fun getItemCount() = rowItems.size

}
