package com.example.dream_ias.repository

import com.example.dream_ias.apiResponse.razorpay.RazorPayPostModel
import com.example.dream_ias.apiResponse.razorpay.RazorPayValidationRes
import com.example.dream_ias.base.BaseRepository
import com.example.dream_ias.network.RazorPayRestClient
import com.example.dream_ias.network.RestClient

object AuthRepository : BaseRepository() {

    suspend fun login(params: HashMap<String, String>) =
        RestClient().getApi().login(params).intoSafe()

    /* suspend fun signUp(params: HashMap<String,String>) = safeApiCall {
         RestClient().getApi().signUp(params)
     }*/
    suspend fun signUp(params: HashMap<String, String>) =
        RestClient().getApi().signUp(params).intoSafe()


    //    suspend fun userDetail(token: String) = safeApiCall {
//        RestClient().getApi().getUserDetail(token)
//    }
    suspend fun userDetail(token: String) = RestClient().getApi().getUserDetail(token).intoSafe()

    /*suspend fun updateUser(token: String, params: HashMap<String, String>) = safeApiCall {
        RestClient().getApi().updateUserDetail(token, params)
    }*/
    suspend fun updateUser(token: String, params: HashMap<String, String>) =
        RestClient().getApi().updateUserDetail(token, params).intoSafe()

    suspend fun privacyPolicy(token: String, type: String) =
        RestClient().getApi().getstatiContent(token, type).intoSafe()

    suspend fun examCategoryList(token: String) =
        RestClient().getApi().getExamCategoryList(token).intoSafe()

    suspend fun faqList(token: String) = RestClient().getApi().getFaqList(token).intoSafe()
    suspend fun myTestList(token: String, examId: String) =
        RestClient().getApi().getTestList(token, examId).intoSafe()

    suspend fun myTestContestList(token: String, testId: String) =
        RestClient().getApi().getTestConyesyList(token, testId).intoSafe()

    suspend fun getContestEntry(token: String, params: HashMap<String, String>) =
        RestClient().getApi().getContestEntryList(token, params).intoSafe()

    suspend fun getMyContest(
        token: String,
        status: String?,
        examcategoryId: String,
        testId: String?
    ) = RestClient().getApi().getMyContestList(token, status, examcategoryId, testId).intoSafe()

    suspend fun getLogOut(token: String) = RestClient().getApi().logOut(token).intoSafe()

    suspend fun addAmount(token: String, amount: Int) =
        RestClient().getApi().addAmount(token, amount).intoSafe()

    suspend fun withDrow(token: String, amount: Int) =
        RestClient().getApi().withDrowAmount(token, amount).intoSafe()

    suspend fun getAmount(token: String) = RestClient().getApi().getAmount(token).intoSafe()
    suspend fun detailsQuestion(token: String, contestId: String) =
        RestClient().getApi().detailsQuestion(token, contestId).intoSafe()

    suspend fun childLeaderBoard(token: String, contestId: String) =
        RestClient().getApi().childLeaderBoard(token, contestId).intoSafe()

    suspend fun contestResults(token: String, contestId: String) =
        RestClient().getApi().contestResults(token, contestId).intoSafe()

    suspend fun practiceTest(token: String, examId: String, status: String) =
        RestClient().getApi().practiceList(token, examId, status).intoSafe()
    suspend fun practiceCompletedTest(token: String, examId: String) =
        RestClient().getApi().practiceCompletedList(token, examId).intoSafe()

    suspend fun planHomeList(token: String, examId: String) =
        RestClient().getApi().planList(token, examId).intoSafe()

    suspend fun planAllAccessHomeList(token: String, examName: String) =
        RestClient().getApi().planAllAccessList(token, examName).intoSafe()

    suspend fun addPlanHome(
        token: String,
        planId: String,
        subsType: String,
        examId: String,
        amount: Int,
        duration: String,
        validity: Int
    ) =
        RestClient().getApi().addPlan(token, planId, subsType, examId, amount, duration, validity)
            .intoSafe()

    suspend fun contestLeaderBoard(token: String, contestId: String) =
        RestClient().getApi().contestLeadboardList(token, contestId).intoSafe()

    suspend fun subScriptionSide(token: String, isExpired: Boolean) =
        RestClient().getApi().subscriptionSideList(token, isExpired).intoSafe()

    suspend fun practiceDetail(token: String, questionId: String) =
        RestClient().getApi().practiceDetail(token, questionId).intoSafe()

    suspend fun startTestPractice(token: String, praticeTestId: String,userId: String) =
        RestClient().getApi().startTestPractice(token, praticeTestId,userId).intoSafe()
    suspend fun saveResponcePractice(token: String, params: HashMap<String, String>) =
        RestClient().getApi().saveResoncePractice(token, params).intoSafe()
    suspend fun resultsPractice(token: String, praticeTestId: String) =
        RestClient().getApi().resultsPractice(token, praticeTestId).intoSafe()
    suspend fun leaderBoardPractice(token: String, praticeTestId: String) =
        RestClient().getApi().practcieLeadboardList(token, praticeTestId).intoSafe()

}