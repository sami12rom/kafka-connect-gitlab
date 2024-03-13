package com.sami12rom.kafka.gitlab

import kotlinx.serialization.Serializable
//import org.apache.avro.Schema
//import org.apache.avro.SchemaBuilder
//import org.apache.avro.generic.GenericData
import org.apache.kafka.connect.data.SchemaBuilder
import org.apache.kafka.connect.data.Schema
import org.apache.kafka.connect.data.Struct
import java.util.Dictionary

class Schemas {
    companion object {
        val mergedRequestValueSchema: Schema = SchemaBuilder.struct()
            .name("com.sami12rom.mergedRequest").version(1).doc("Merged Request Value Schema")
            .field("id", SchemaBuilder.int64())
            .field("iid", SchemaBuilder.int64())
            .field("project_id", SchemaBuilder.int64())
            .field("title", SchemaBuilder.string().optional().defaultValue(null))
            .field("description", SchemaBuilder.string().optional().defaultValue(null))
            .field("state", SchemaBuilder.string().optional().defaultValue(null))
            .field("created_at", SchemaBuilder.string().optional().defaultValue(null))
            .field("updated_at", SchemaBuilder.string().optional().defaultValue(null))
            .field("merged_by", SchemaBuilder.struct().name("merged_by").optional().defaultValue(null)
                .field("id", SchemaBuilder.int32().optional().defaultValue(null))
                .field("username", SchemaBuilder.string().optional().defaultValue(null))
                .field("name", SchemaBuilder.string().optional().defaultValue(null))
                .field("state", SchemaBuilder.string().optional().defaultValue(null))
                .field("locked", SchemaBuilder.bool().optional().defaultValue(null))
                .field("avatar_url", SchemaBuilder.string().optional().defaultValue(null))
                .field("web_url", SchemaBuilder.string().optional().defaultValue(null))
                .build())
            .field("merge_user", SchemaBuilder.struct().name("merge_user").optional().defaultValue(null)
                .field("id", SchemaBuilder.int32().optional().defaultValue(null))
                .field("username", SchemaBuilder.string().optional().defaultValue(null))
                .field("name", SchemaBuilder.string().optional().defaultValue(null))
                .field("state", SchemaBuilder.string().optional().defaultValue(null))
                .field("locked", SchemaBuilder.bool().optional().defaultValue(null))
                .field("avatar_url", SchemaBuilder.string().optional().defaultValue(null))
                .field("web_url", SchemaBuilder.string().optional().defaultValue(null))
                .build())
            .field("merged_at", SchemaBuilder.string().optional().defaultValue(null))
            .field("closed_by", SchemaBuilder.struct().name("closed_by").optional().defaultValue(null)
                .field("id", SchemaBuilder.int32().optional().defaultValue(null))
                .field("username", SchemaBuilder.string().optional().defaultValue(null))
                .field("name", SchemaBuilder.string().optional().defaultValue(null))
                .field("state", SchemaBuilder.string().optional().defaultValue(null))
                .field("locked", SchemaBuilder.bool().optional().defaultValue(null))
                .field("avatar_url", SchemaBuilder.string().optional().defaultValue(null))
                .field("web_url", SchemaBuilder.string().optional().defaultValue(null))
                .build())
            .field("closed_at", SchemaBuilder.string().optional().defaultValue(null))
            .field("target_branch", SchemaBuilder.string().optional().defaultValue(null))
            .field("source_branch", SchemaBuilder.string().optional().defaultValue(null))
            .field("user_notes_count", SchemaBuilder.int64().optional().defaultValue(null))
            .field("upvotes", SchemaBuilder.int64().optional().defaultValue(null))
            .field("downvotes", SchemaBuilder.int64().optional().defaultValue(null))
            .field("author", SchemaBuilder.struct().name("author").optional().defaultValue(null)
                .field("id", SchemaBuilder.int32().optional().defaultValue(null))
                .field("username", SchemaBuilder.string().optional().defaultValue(null))
                .field("name", SchemaBuilder.string().optional().defaultValue(null))
                .field("state", SchemaBuilder.string().optional().defaultValue(null))
                .field("locked", SchemaBuilder.bool().optional().defaultValue(null))
                .field("avatar_url", SchemaBuilder.string().optional().defaultValue(null))
                .field("web_url", SchemaBuilder.string().optional().defaultValue(null))
                .build())
            .field("assignees", SchemaBuilder.array(SchemaBuilder.struct().name("assignees").optional().defaultValue(null)
                .field("id", SchemaBuilder.int32().optional().defaultValue(null))
                .field("username", SchemaBuilder.string().optional().defaultValue(null))
                .field("name", SchemaBuilder.string().optional().defaultValue(null))
                .field("state", SchemaBuilder.string().optional().defaultValue(null))
                .field("locked", SchemaBuilder.bool().optional().defaultValue(null))
                .field("avatar_url", SchemaBuilder.string().optional().defaultValue(null))
                .field("web_url", SchemaBuilder.string().optional().defaultValue(null))
                .build()).optional().defaultValue(null))
            .field("assignee", SchemaBuilder.struct().name("assignee").optional().defaultValue(null)
                .field("id", SchemaBuilder.int32().optional().defaultValue(null))
                .field("username", SchemaBuilder.string().optional().defaultValue(null))
                .field("name", SchemaBuilder.string().optional().defaultValue(null))
                .field("state", SchemaBuilder.string().optional().defaultValue(null))
                .field("locked", SchemaBuilder.bool().optional().defaultValue(null))
                .field("avatar_url", SchemaBuilder.string().optional().defaultValue(null))
                .field("web_url", SchemaBuilder.string().optional().defaultValue(null))
                .build())
            .field("reviewers", SchemaBuilder.array(SchemaBuilder.struct().name("reviewers").optional().defaultValue(null)
                .field("id", SchemaBuilder.int32().optional().defaultValue(null))
                .field("username", SchemaBuilder.string().optional().defaultValue(null))
                .field("name", SchemaBuilder.string().optional().defaultValue(null))
                .field("state", SchemaBuilder.string().optional().defaultValue(null))
                .field("locked", SchemaBuilder.bool().optional().defaultValue(null))
                .field("avatar_url", SchemaBuilder.string().optional().defaultValue(null))
                .field("web_url", SchemaBuilder.string().optional().defaultValue(null))
                .build()).optional().defaultValue(null))
            .field("source_project_id", SchemaBuilder.int64().optional().defaultValue(null))
            .field("target_project_id", SchemaBuilder.int64().optional().defaultValue(null))
            .field("labels", SchemaBuilder.array(SchemaBuilder.string()).optional().defaultValue(null))
            .field("draft", SchemaBuilder.bool().optional().defaultValue(false))
            .field("work_in_progress", SchemaBuilder.bool().optional().defaultValue(false))
            .field("milestone", SchemaBuilder.string().optional().defaultValue(null))
            .field("merge_when_pipeline_succeeds", SchemaBuilder.bool().optional().defaultValue(false))
            .field("merge_status", SchemaBuilder.string().optional().defaultValue(null))
            .field("detailed_merge_status", SchemaBuilder.string().optional().defaultValue(null))
            .field("sha", SchemaBuilder.string().optional().defaultValue(null))
            .field("merge_commit_sha", SchemaBuilder.string().optional().defaultValue(null))
            .field("squash_commit_sha", SchemaBuilder.string().optional().defaultValue(null))
            .field("discussion_locked", SchemaBuilder.string().optional().defaultValue(null))
            .field("should_remove_source_branch", SchemaBuilder.bool().optional().defaultValue(false))
            .field("force_remove_source_branch", SchemaBuilder.bool().optional().defaultValue(false))
            .field("prepared_at", SchemaBuilder.string().optional().defaultValue(null))
            .field("reference", SchemaBuilder.string().optional().defaultValue(null))
            .field("references", SchemaBuilder.struct().name("references").optional().defaultValue(null)
                .field("short", SchemaBuilder.string().optional().defaultValue(null))
                .field("relative", SchemaBuilder.string().optional().defaultValue(null))
                .field("full", SchemaBuilder.string().optional().defaultValue(null))
                .build())
            .field("web_url", SchemaBuilder.string().optional().defaultValue(null))
            .field("time_stats", SchemaBuilder.struct().name("time_stats").optional().defaultValue(null)
                .field("time_estimate", SchemaBuilder.int32().optional().defaultValue(null))
                .field("total_time_spent", SchemaBuilder.int32().optional().defaultValue(null))
                .field("human_time_estimate", SchemaBuilder.string().optional().defaultValue(null))
                .field("human_total_time_spent", SchemaBuilder.string().optional().defaultValue(null))
                .build())
            .field("squash", SchemaBuilder.bool().optional().defaultValue(false))
            .field("squash_on_merge", SchemaBuilder.bool().optional().defaultValue(false))
            .field("task_completion_status", SchemaBuilder.struct().name("task_completion_status").optional().defaultValue(null)
                .field("count", SchemaBuilder.int32().optional().defaultValue(null))
                .field("completed_count", SchemaBuilder.int32().optional().defaultValue(null))
                .build())
            .field("has_conflicts", SchemaBuilder.bool().optional().defaultValue(false))
            .field("blocking_discussions_resolved", SchemaBuilder.bool().optional().defaultValue(false))
            .field("approvals_before_merge", SchemaBuilder.int64().optional().defaultValue(null))
            .build()
    }
}

class Structs {
    fun structMergedRequestValue(mergedRequest: MergedRequest): Struct {
        val struct = Struct(Schemas.mergedRequestValueSchema)
        struct.put("id", mergedRequest.id)
        struct.put("iid", mergedRequest.iid)
        struct.put("project_id", mergedRequest.project_id)
        struct.put("title", mergedRequest.title)
        struct.put("description", mergedRequest.description)
        struct.put("state", mergedRequest.state)
        struct.put("created_at", mergedRequest.created_at)
        struct.put("updated_at", mergedRequest.updated_at)
        struct.put("merged_by", Struct(Schemas.mergedRequestValueSchema.field("merged_by").schema())
            .put("id", mergedRequest.merged_by?.id)
            .put("username", mergedRequest.merged_by?.username)
            .put("name", mergedRequest.merged_by?.name)
            .put("state", mergedRequest.merged_by?.state)
            .put("locked", mergedRequest.merged_by?.locked)
            .put("avatar_url", mergedRequest.merged_by?.avatar_url)
            .put("web_url", mergedRequest.merged_by?.web_url))
        struct.put("merge_user", Struct(Schemas.mergedRequestValueSchema.field("merge_user").schema())
            .put("id", mergedRequest.merge_user?.id)
            .put("username", mergedRequest.merge_user?.username)
            .put("name", mergedRequest.merge_user?.name)
            .put("state", mergedRequest.merge_user?.state)
            .put("locked", mergedRequest.merge_user?.locked)
            .put("avatar_url", mergedRequest.merge_user?.avatar_url)
            .put("web_url", mergedRequest.merge_user?.web_url))
        struct.put("merged_at", mergedRequest.merged_at)
        struct.put("closed_by", Struct(Schemas.mergedRequestValueSchema.field("closed_by").schema())
            .put("id", mergedRequest.closed_by?.id)
            .put("username", mergedRequest.closed_by?.username)
            .put("name", mergedRequest.closed_by?.name)
            .put("state", mergedRequest.closed_by?.state)
            .put("locked", mergedRequest.closed_by?.locked)
            .put("avatar_url", mergedRequest.closed_by?.avatar_url)
            .put("web_url", mergedRequest.closed_by?.web_url))
        struct.put("closed_at", mergedRequest.closed_at)
        struct.put("target_branch", mergedRequest.target_branch)
        struct.put("source_branch", mergedRequest.source_branch)
        struct.put("user_notes_count", mergedRequest.user_notes_count)
        struct.put("upvotes", mergedRequest.upvotes)
        struct.put("downvotes", mergedRequest.downvotes)
        struct.put("author", Struct(Schemas.mergedRequestValueSchema.field("author").schema())
            .put("id", mergedRequest.author?.id)
            .put("username", mergedRequest.author?.username)
            .put("name", mergedRequest.author?.name)
            .put("state", mergedRequest.author?.state)
            .put("locked", mergedRequest.author?.locked)
            .put("avatar_url", mergedRequest.author?.avatar_url)
            .put("web_url", mergedRequest.author?.web_url))
//        struct.put("assignees", List<Struct> (mergedRequest.assignees?.size?:0) {
//            Struct(Schemas.mergedRequestValueSchema.field("assignees").schema())})
        struct.put("assignee", Struct(Schemas.mergedRequestValueSchema.field("assignee").schema())
            .put("id", mergedRequest.assignee?.id)
            .put("username", mergedRequest.assignee?.username)
            .put("name", mergedRequest.assignee?.name)
            .put("state", mergedRequest.assignee?.state)
            .put("locked", mergedRequest.assignee?.locked)
            .put("avatar_url", mergedRequest.assignee?.avatar_url)
            .put("web_url", mergedRequest.assignee?.web_url))
//        struct.put("reviewers", mergedRequest.reviewers)
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
        struct.put("references", Struct(Schemas.mergedRequestValueSchema.field("references").schema())
            .put("short", mergedRequest.references?.short)
            .put("relative", mergedRequest.references?.relative)
            .put("full", mergedRequest.references?.full))
        struct.put("web_url", mergedRequest.web_url)
        struct.put("time_stats", Struct(Schemas.mergedRequestValueSchema.field("time_stats").schema())
            .put("time_estimate", mergedRequest.time_stats?.time_estimate)
            .put("total_time_spent", mergedRequest.time_stats?.total_time_spent)
            .put("human_time_estimate", mergedRequest.time_stats?.human_time_estimate)
            .put("human_total_time_spent", mergedRequest.time_stats?.human_total_time_spent))
        struct.put("squash", mergedRequest.squash)
        struct.put("squash_on_merge", mergedRequest.squash_on_merge)
        struct.put("task_completion_status", Struct(Schemas.mergedRequestValueSchema.field("task_completion_status").schema())
            .put("count", mergedRequest.task_completion_status?.count)
            .put("completed_count", mergedRequest.task_completion_status?.completed_count))
        struct.put("has_conflicts", mergedRequest.has_conflicts)
        struct.put("blocking_discussions_resolved", mergedRequest.blocking_discussions_resolved)
        struct.put("approvals_before_merge", mergedRequest.approvals_before_merge)
        return struct
    }
}

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