package com.sami12rom.kafka.gitlab.model

import kotlinx.serialization.Serializable

@Serializable
data class MergedRequest(
    val id: Long,
    val iid: Long? = null,
    val project_id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val state: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val merged_by: User? = null,
    val merge_user: User? = null,
    val merged_at: String? = null,
    val closed_by: User? = null,
    val closed_at: String? = null,
    val target_branch: String? = null,
    val source_branch: String? = null,
    val user_notes_count: Long? = null,
    val upvotes: Long? = null,
    val downvotes: Long? = null,
    val author: User? = null,
    val assignees: List<User>? = emptyList(),
    val assignee: User? = null,
    val reviewers: List<User?>? = emptyList(),
    val source_project_id: Long? = null,
    val target_project_id: Long? = null,
    val labels: List<String?>? = emptyList(),
    val draft: Boolean? = null,
    val work_in_progress: Boolean? = null,
    val milestone: String?,
    val merge_when_pipeline_succeeds: Boolean? = null,
    val merge_status: String? = null,
    val detailed_merge_status: String? = null,
    val sha: String? = null,
    val merge_commit_sha: String? = null,
    val squash_commit_sha: String? = null,
    val discussion_locked: String?,
    val should_remove_source_branch: Boolean? = null,
    val force_remove_source_branch: Boolean? = null,
    val prepared_at: String? = null,
    val reference: String? = null,
    val references: References? = null,
    val web_url: String? = null,
    val time_stats: TimeStats? = null,
    val squash: Boolean? = null,
    val squash_on_merge: Boolean? = null,
    val task_completion_status: TaskCompletionStatus? = null,
    val has_conflicts: Boolean? = null,
    val blocking_discussions_resolved: Boolean? = null,
    val approvals_before_merge: String? = null
)

@Serializable
data class User(
    val id: Int? = null,
    val username: String? = null,
    val name: String? = null,
    val state: String? = null,
    val locked: Boolean? = null,
    val avatar_url: String? = null,
    val web_url: String? = null
)

@Serializable
data class References(
    val short: String? = null,
    val relative: String? = null,
    val full: String? = null
)
@Serializable
data class TimeStats(
    val time_estimate: Int? = null,
    val total_time_spent: Int? = null,
    val human_time_estimate: String? = null,
    val human_total_time_spent: String? = null
)

@Serializable
data class TaskCompletionStatus(
    val count: Int? = null,
    val completed_count: Int? = null
)