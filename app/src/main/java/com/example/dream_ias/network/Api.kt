package com.example.dream_ias.network

import com.example.dream_ias.apiResponse.*
import com.example.dream_ias.apiResponse.leaderBoard.ChildLeaderBoardList
import com.example.dream_ias.apiResponse.leaderBoard.ContestMainLeaderBoardRes
import com.example.dream_ias.apiResponse.plan.AddPlanHomeRes
import com.example.dream_ias.apiResponse.plan.HomePlanRes
import com.example.dream_ias.apiResponse.plan.SideSubscriptionRes
import com.example.dream_ias.apiResponse.practiceTest.*
import com.example.dream_ias.apiResponse.razorpay.RazorPayPostModel
import com.example.dream_ias.apiResponse.razorpay.RazorPayValidationRes
import com.example.dream_ias.apiResponse.results.ResultsListRes
import com.example.dream_ias.util.Constants
import retrofit2.Response
import retrofit2.http.*

interface Api {

//    @POST("home")
//    suspend fun getHome(): Response<String>

    @POST(Constants.LOGIN)
    suspend fun login(@Body data: HashMap<String, String>): Response<LoginResponse?>

    @POST(Constants.SIGNUP)
    suspend fun signUp(@Body data: HashMap<String, String>): Response<LoginResponse?>//SignUpResponse this resoponse change for testing

    @POST(Constants.UPDATE_PROFILE)
    suspend fun updateUserDetail(
        @Header("Authorization") auth: String,
        @Body data: HashMap<String, String>): Response<LoginResponse?>

//    @GET(Constants.DELETE)
//    suspend fun crewLeveList(
//        @Header("Authorization") auth: String
//    ): LoginResponse?

    @GET(Constants.LIST)
    suspend fun getUserDetail(
        @Header("Authorization") auth: String
    ): Response<LoginResponse?>

    @GET(Constants.STATIC_CONTENT)
    suspend fun getstatiContent(
        @Header("Authorization") auth: String,
        @Query("type") type: String
    ): Response<PrivacyandPolicyRes?>

    @GET(Constants.EXAM_CATEGORY_LIST)
    suspend fun getExamCategoryList(
        @Header("Authorization") auth: String,
    ): Response<ExamNameListRes?>

    @GET(Constants.FAQ_LIST)
    suspend fun getFaqList(
        @Header("Authorization") auth: String,
    ): Response<FAQListRes?>

    @GET(Constants.TEST_LIST)
    suspend fun getTestList(
        @Header("Authorization") auth: String,
        @Query("examId") examId: String
    ): Response<MyTestListRes?>

    @GET(Constants.TEST_CONTEST)
    suspend fun getTestConyesyList(
        @Header("Authorization") auth: String,
        @Query("testId") testId: String
    ): Response<TestContestListRes?>

    @POST(Constants.CONTEST_ENTRY)
    suspend fun getContestEntryList(
        @Header("Authorization") auth: String,
        @Body data: HashMap<String, String>
    ): Response<CommonRes?>

    @GET(Constants.LOGOUT)
    suspend fun logOut(
        @Header("Authorization") auth: String
    ): Response<CommonRes?>

    @GET(Constants.MYCONTEST)
    suspend fun getMyContestList(
        @Header("Authorization") auth: String,
        @Query("status") status:String?,
        @Query("examcategoryId") examcategoryId:String,
        @Query("testId") testId:String?
    ): Response<MyTestMainFragListRes?>

    @POST(Constants.ADD_AMOUNT)
    @FormUrlEncoded
    suspend fun addAmount(
        @Header("Authorization") auth: String,
        @Field("amount") amount:Int
    ): Response<CommonRes?>

    @POST(Constants.WITHDRAW)
    @FormUrlEncoded
    suspend fun withDrowAmount(
        @Header("Authorization") auth: String,
        @Field("amount") amount:Int
    ): Response<CommonRes?>

    @GET(Constants.AMOUNT)
    suspend fun getAmount(
        @Header("Authorization") auth: String
    ): Response<GetAmountRes?>

    @GET(Constants.DETAILS)
    suspend fun detailsQuestion(
        @Header("Authorization") auth: String,
        @Query("contestId") contestId:String
    ): Response<DetailsQuestionList?>

    @GET(Constants.CHILD_LEADER_BOARD)
    suspend fun childLeaderBoard(
        @Header("Authorization") auth: String,
        @Query("contestId") contestId: String
    ): Response<ChildLeaderBoardList?>

    @POST(Constants.CONTEST_RESULTS)
    suspend fun contestResults(
        @Header("Authorization") auth: String,
        @Query("contestId") contestId: String
    ): Response<ResultsListRes?>

    @GET(Constants.PRACTICE_LIST)
    suspend fun practiceList(
        @Header("Authorization") auth: String,
        @Query("examId") examId: String,
        @Query("status") status: String
    ): Response<OngoingPracticeTestRes?>

    @GET(Constants.PRACTICE_COMPLETED)
    suspend fun practiceCompletedList(
        @Header("Authorization") auth: String,
        @Query("examId") examId: String
    ): Response<CompletedPracticeTestRes?>

    @GET(Constants.PLAN_LIST)
    suspend fun planList(
        @Header("Authorization") auth: String,
        @Query("examId") examId: String
    ): Response<HomePlanRes?>
    @GET(Constants.PLAN_LIST)
    suspend fun planAllAccessList(
        @Header("Authorization") auth: String,
        @Query("examName") examName: String
    ): Response<HomePlanRes?>

    @POST(Constants.ADD_PLAN)
    @FormUrlEncoded
    suspend fun addPlan(
        @Header("Authorization") auth: String,
        @Field("planId") planId: String,
        @Field("subsType") subsType: String,
        @Field("examId") examId: String,
        @Field("amount") amount: Int,
        @Field("duration") duration: String,
        @Field("validity") validity: Int
    ): Response<AddPlanHomeRes?>

    @GET(Constants.CONTEST_LEADER_BOARD)
    suspend fun contestLeadboardList(
        @Header("Authorization") auth: String,
        @Query("contestId") contestId: String
    ): Response<ContestMainLeaderBoardRes?>

    @GET(Constants.SUB)
    suspend fun subscriptionSideList(
        @Header("Authorization") auth: String,
        @Query("isExpired") isExpired: Boolean
    ): Response<SideSubscriptionRes?>

    @GET(Constants.PRACTICE_DETAIL)
    suspend fun practiceDetail(
        @Header("Authorization") auth: String,
        @Query("questionId") questionId: String
    ): Response<DetailPracticeTestRes?>

    @POST(Constants.PRACTICE_START)
    @FormUrlEncoded
    suspend fun startTestPractice(
        @Header("Authorization") auth: String,
        @Field("praticeTestId") praticeTestId: String,
        @Field("userId") userId: String
    ): Response<StartTestPracticeRes?>

    @POST(Constants.SAVE_RESPONSE)
    suspend fun saveResoncePractice(
        @Header("Authorization") auth: String,
        @Body data: HashMap<String, String>
    ): Response<SaveReponcePracticeRes?>

    @GET(Constants.PRACTICE_RESULT)
    suspend fun resultsPractice(
        @Header("Authorization") auth: String,
        @Query("praticeTestId") praticeTestId: String
    ): Response<ResultsPracticeRes?>

    @GET(Constants.PRACTICE_LEARDER_BOARD)
    suspend fun practcieLeadboardList(
        @Header("Authorization") auth: String,
        @Query("praticeTestId") praticeTestId: String
    ): Response<ContestMainLeaderBoardRes?>




}

