package com.thinlineit.ctrlf.repository

import com.thinlineit.ctrlf.issue.IssueDao
import com.thinlineit.ctrlf.repository.network.IssueService

class IssueRepository {

    suspend fun loadIssueList(): List<IssueDao> {
        return getIssueList()
    }

    fun getmockIssueList(): List<IssueDao> {
        // TODO : Room 사용시 api로 로드할것인지, Room에서 로드할것인지 로직 구현
        val contentStr =
            "가나다라마바사아자차카타파하가나다라마바사아자차카타파하" +
                "가나다라마바사아자차카타파하가나다라마바사아자차카타"
        return (1..9).map { i ->
            if (i % 2 != 0) IssueDao(
                i,
                "rkdmf1026@naver.com",
                "title$i",
                contentStr + contentStr,
                "Requested"
            )
            else IssueDao(
                i,
                "rkdmf1026@naver.com",
                "title$i",
                contentStr + contentStr + contentStr,
                "Requested"
            )
        }
    }

    private suspend fun getIssueList(): List<IssueDao> {
        return try {
            val issue = IssueService.retrofitService.listIssue(0).issues!!
            issue
        } catch (e: Exception) {
            emptyList()
        }
    }
}
