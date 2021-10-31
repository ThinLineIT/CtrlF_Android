package com.thinlineit.ctrlf.repository.dao

import com.thinlineit.ctrlf.entity.Issue
import com.thinlineit.ctrlf.repository.dto.request.IssueApproveRequest
import com.thinlineit.ctrlf.repository.network.IssueService

class IssueRepository {

    suspend fun loadIssueList(): List<Issue> {
        return getIssueList()
    }

    private suspend fun getIssueList(): List<Issue> {
        return try {
            val issueList = IssueService.retrofitService.listIssue(0).issues!!
            issueList
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getIssueDetail(issueId: String): Issue {
        return IssueService.retrofitService.DetailIssue(issueId)
    }

    suspend fun approveIssue(issueId: Int): String {
        return try {
            IssueService.retrofitService.ApproveIssue(
                "Bearer " + com.thinlineit.ctrlf.util.Application.preferenceUtil.getString(
                    TOKEN,
                    ""
                ),
                IssueApproveRequest(issueId)
            ).message()
        } catch (e: Exception) {
            e.toString()
        }
    }

    companion object {
        private const val TOKEN = "token"
    }
}
