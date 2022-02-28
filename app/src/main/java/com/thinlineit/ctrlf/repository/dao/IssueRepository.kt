package com.thinlineit.ctrlf.repository.dao

import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.repository.dto.request.IssueApproveRequest
import com.thinlineit.ctrlf.repository.network.IssueService
import com.thinlineit.ctrlf.util.Application

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

    suspend fun approveIssue(issueId: Int): Int {
        return try {
            IssueService.retrofitService.approveIssue(
                "Bearer " + Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                IssueApproveRequest(issueId)
            ).code()
        } catch (e: Exception) {
            SERVER_ERROR
        }
    }

    suspend fun rejectIssue(issueId: Int): Int {
        return try {
            // TODO : Set rejectIssue API
            200
        } catch (e: Exception) {
            SERVER_ERROR
        }
    }

    suspend fun deleteIssue(issueId: Int): Int {
        return try {
            // TODO : Set deleteIssue API
            200
        } catch (e: Exception) {
            SERVER_ERROR
        }
    }

    suspend fun closeIssue(issueId: Int): Int {
        return try {
            // TODO : Set closeIssue API
            200
        } catch (e: Exception) {
            SERVER_ERROR
        }
    }

    companion object {
        const val TOKEN = "token"
        const val SERVER_ERROR = 500
    }
}
