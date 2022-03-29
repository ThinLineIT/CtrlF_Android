package com.thinlineit.ctrlf.repository.dao

import android.util.Log
import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.repository.dto.request.IssueActionRequest
import com.thinlineit.ctrlf.repository.dto.request.IssueUpdateActionRequest
import com.thinlineit.ctrlf.repository.network.IssueService
import com.thinlineit.ctrlf.util.Application
import java.net.ProtocolException

class IssueRepository {

    suspend fun loadIssueList(): List<Issue> {
        return getIssueListViaNetwork()
    }

    private suspend fun getIssueListViaNetwork(): List<Issue> {
        return try {
            val issueList = IssueService.retrofitService.listIssue(0).issues!!
            issueList
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getIssueDetail(issueId: String): Issue =
        IssueService.retrofitService.detailIssue(issueId)

    suspend fun getIssueCount(): Int = IssueService.retrofitService.issueCount().issuesCount

    suspend fun approveIssue(issueId: Int): Boolean {
        return try {
            IssueService.retrofitService.approveIssue(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                IssueActionRequest(issueId)
            ).code()
            true
        } catch (e: ProtocolException) {
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun rejectIssue(issueId: Int): Int {
        return try {
            IssueService.retrofitService.rejectIssue(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                IssueActionRequest(issueId)
            ).code()
        } catch (e: Exception) {
            SERVER_ERROR
        }
    }

    suspend fun deleteIssue(issueId: Int): Int {
        return try {
            IssueService.retrofitService.deleteIssue(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                IssueActionRequest(issueId)
            ).code()
        } catch (e: Exception) {
            SERVER_ERROR
        }
    }

    suspend fun closeIssue(issueId: Int): Int {
        return try {
            IssueService.retrofitService.closeIssue(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                IssueActionRequest(issueId)
            ).code()
        } catch (e: Exception) {
            SERVER_ERROR
        }
    }

    suspend fun updateIssue(
        issueId: Int,
        newTitle: String? = null,
        newContent: String? = null,
        reason: String
    ): Boolean {
        return try {
            val requestBody =
                IssueUpdateActionRequest(issueId, newTitle, newContent, reason)
            IssueService.retrofitService.updateIssue(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                requestBody
            )
            true
        } catch (e: ProtocolException) {
            Log.d("issueRepository", "fail1$e")
            false
        } catch (e: Exception) {
            Log.d("issueRepository", "fail2$e")
            false
        }
    }

    suspend fun checkIssuePermission(issueId: Int): Boolean {
        return try {
            IssueService.retrofitService.checkPermissionIssue(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                IssueActionRequest(issueId)
            ).hasPermission
        } catch (e: ProtocolException) {
            Log.d("issueRepository", "$e")
            false
        } catch (e: Exception) {
            Log.d("issueRepository", "$e")
            false
        }
    }

    companion object {
        const val TOKEN = "token"
        const val SERVER_ERROR = 500
    }
}
