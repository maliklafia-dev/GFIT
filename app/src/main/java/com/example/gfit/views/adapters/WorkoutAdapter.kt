package com.example.gfit.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gfit.data.model.workout.WorkoutExercise
import com.example.gfit.databinding.ItemViewBinding

class WorkoutAdapter : ListAdapter<WorkoutExercise, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    class WorkoutViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutExercise) {
            binding.apply {
                cardWorkoutTitleTxtView.text = workout.title
                cardWorkoutDay.text = workout.day
                cardWorkoutDuration.text = "${workout.duration} min"

                Glide.with(root.context)
                    .load(workout.imageUrl)
                    .centerCrop()
                    .into(cardWorkoutImageImgView)
            }
        }

        companion object {
            fun from(parent: ViewGroup): WorkoutViewHolder {
                val binding = ItemViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return WorkoutViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class WorkoutDiffCallback : DiffUtil.ItemCallback<WorkoutExercise>() {
    override fun areItemsTheSame(oldItem: WorkoutExercise, newItem: WorkoutExercise): Boolean {
        return oldItem.title == newItem.title // Ou utilisez un ID unique si disponible
    }

    override fun areContentsTheSame(oldItem: WorkoutExercise, newItem: WorkoutExercise): Boolean {
        return oldItem == newItem
    }
}