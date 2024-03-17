package com.sami12rom.kafka.gitlab

import com.sami12rom.kafka.gitlab.model.*
import org.apache.kafka.connect.data.Struct

class Structs {
    companion object{
        fun userStruct(user: User?): Struct {
            return Struct(Schemas.userSchema)
                .put("id", user?.id)
                .put("username", user?.username)
                .put("name", user?.name)
                .put("state", user?.state)
                .put("locked", user?.locked)
                .put("avatar_url", user?.avatar_url)
                .put("web_url", user?.web_url)
        }

        fun timeStatsStruct(timeStats: TimeStats?): Struct {
            return Struct(Schemas.timeStatsSchema)
                .put("time_estimate", timeStats?.time_estimate)
                .put("total_time_spent", timeStats?.total_time_spent)
                .put("human_time_estimate", timeStats?.human_time_estimate)
                .put("human_total_time_spent", timeStats?.human_total_time_spent)
        }

        fun referenceStruct(reference: References?): Struct {
            return Struct(Schemas.referenceSchema)
                .put("short", reference?.short)
                .put("relative", reference?.relative)
                .put("full", reference?.full)
        }

        fun timeCompletionStatusStruct(taskCompletionStatus: TaskCompletionStatus?): Struct {
            return Struct(Schemas.taskCompletionStatusSchema)
                .put("count", taskCompletionStatus?.count)
                .put("completed_count", taskCompletionStatus?.completed_count)
        }

        fun mergedRequestKeyStruct(mergedRequest: MergedRequest): Struct {
            val struct = Struct(Schemas.mergedRequestKeySchema)
            struct.put("project_id", mergedRequest.project_id)
            return struct
        }
        fun mergedRequestValueStruct(mergedRequest: MergedRequest): Struct {
            val struct = Struct(Schemas.mergedRequestValueSchema)
            struct.put("id", mergedRequest.id)
            struct.put("iid", mergedRequest.iid)
            struct.put("project_id", mergedRequest.project_id)
            struct.put("title", mergedRequest.title)
            struct.put("description", mergedRequest.description)
            struct.put("state", mergedRequest.state)
            struct.put("created_at", mergedRequest.created_at)
            struct.put("updated_at", mergedRequest.updated_at)
            struct.put("merged_by", userStruct(mergedRequest.merged_by))
            struct.put("merge_user", userStruct(mergedRequest.merge_user))
            struct.put("merged_at", mergedRequest.merged_at)
            struct.put("closed_by", userStruct(mergedRequest.closed_by))
            struct.put("closed_at", mergedRequest.closed_at)
            struct.put("target_branch", mergedRequest.target_branch)
            struct.put("source_branch", mergedRequest.source_branch)
            struct.put("user_notes_count", mergedRequest.user_notes_count)
            struct.put("upvotes", mergedRequest.upvotes)
            struct.put("downvotes", mergedRequest.downvotes)
            struct.put("author", userStruct(mergedRequest.author))
            struct.put("assignees", mergedRequest.assignees?.map { user -> userStruct(user) } ?: emptyList<Struct>())
            struct.put("assignee", userStruct(mergedRequest.assignee))
            struct.put("reviewers", mergedRequest.reviewers?.map { user -> userStruct(user) } ?: emptyList<Struct>())
            struct.put("source_project_id", mergedRequest.source_project_id)
            struct.put("target_project_id", mergedRequest.target_project_id)
            struct.put("labels", mergedRequest.labels)
            struct.put("draft", mergedRequest.draft)
            struct.put("work_in_progress", mergedRequest.work_in_progress)
            struct.put("milestone", mergedRequest.milestone)
            struct.put("merge_when_pipeline_succeeds", mergedRequest.merge_when_pipeline_succeeds)
            struct.put("merge_status", mergedRequest.merge_status)
            struct.put("detailed_merge_status", mergedRequest.detailed_merge_status)
            struct.put("sha", mergedRequest.sha)
            struct.put("merge_commit_sha", mergedRequest.merge_commit_sha)
            struct.put("squash_commit_sha", mergedRequest.squash_commit_sha)
            struct.put("discussion_locked", mergedRequest.discussion_locked)
            struct.put("should_remove_source_branch", mergedRequest.should_remove_source_branch)
            struct.put("force_remove_source_branch", mergedRequest.force_remove_source_branch)
            struct.put("prepared_at", mergedRequest.prepared_at)
            struct.put("reference", mergedRequest.reference)
            struct.put("references", referenceStruct(mergedRequest.references))
            struct.put("web_url", mergedRequest.web_url)
            struct.put("time_stats", timeStatsStruct(mergedRequest.time_stats))
            struct.put("squash", mergedRequest.squash)
            struct.put("squash_on_merge", mergedRequest.squash_on_merge)
            struct.put("task_completion_status", timeCompletionStatusStruct(mergedRequest.task_completion_status))
            struct.put("has_conflicts", mergedRequest.has_conflicts)
            struct.put("blocking_discussions_resolved", mergedRequest.blocking_discussions_resolved)
            struct.put("approvals_before_merge", mergedRequest.approvals_before_merge)
            return struct
        }
    }
}