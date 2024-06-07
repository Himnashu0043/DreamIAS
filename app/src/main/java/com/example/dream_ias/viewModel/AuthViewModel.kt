package com.example.dream_ias.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.dream_ias.repository.AuthRepository
import com.example.dream_ias.util.Resource
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    private val _userLogin: MutableLiveData<Resource<LoginResponse?>?> = MutableLiveData()
    val userLogin: LiveData<Resource<LoginResponse?>?> get() = _userLogin
    suspend fun login(params:HashMap<String,String>){
        _userLogin.value = Resource.Loading
        _userLogin.value = AuthRepository.login(params)
    }

    private val _userSignUp: MutableLiveData<Resource<LoginResponse?>?> = MutableLiveData()
    val userSignUp: LiveData<Resource<LoginResponse?>?> get() = _userSignUp
    suspend fun signUp(params:HashMap<String,String>){
        _userSignUp.value = Resource.Loading
        _userSignUp.value = AuthRepository.signUp(params)
    }

    private val _userDetail: MutableLiveData<Resource<LoginResponse?>?> = MutableLiveData()
    val userDetail: LiveData<Resource<LoginResponse?>?> get() = _userDetail
    suspend fun userDetail(token: String){
        _userDetail.value = Resource.Loading
        _userDetail.value = AuthRepository.userDetail(token)
    }

    private val _updateUserDetail: MutableLiveData<Resource<LoginResponse?>?> = MutableLiveData()
    val updateUserDetail: LiveData<Resource<LoginResponse?>?> get() = _updateUserDetail
    fun updateUserDetail(token: String, params: HashMap<String, String>) {
        viewModelScope.launch {
            _updateUserDetail.value = Resource.Loading
            _updateUserDetail.value = AuthRepository.updateUser(token, params)
        }
    }

    private val _privacyPolicy: MutableLiveData<Resource<PrivacyandPolicyRes?>?> = MutableLiveData()
    val _privacyPolicyList: LiveData<Resource<PrivacyandPolicyRes?>?> get() = _privacyPolicy
    fun _privacyPolicyList(token: String, type: String) {
        viewModelScope.launch {
            _privacyPolicy.value = Resource.Loading
            _privacyPolicy.value = AuthRepository.privacyPolicy(token, type)
        }
    }

    private val _examCategoryList: MutableLiveData<Resource<ExamNameListRes?>?> = MutableLiveData()
    val _examcategoryList: LiveData<Resource<ExamNameListRes?>?> get() = _examCategoryList
    fun _exam_CategoryList(token: String) {
        viewModelScope.launch {
            _examCategoryList.value = Resource.Loading
            _examCategoryList.value = AuthRepository.examCategoryList(token)
        }
    }

    private val _faqList: MutableLiveData<Resource<FAQListRes?>?> = MutableLiveData()
    val _faqListLiveData: LiveData<Resource<FAQListRes?>?> get() = _faqList
    fun _faq(token: String) {
        viewModelScope.launch {
            _faqList.value = Resource.Loading
            _faqList.value = AuthRepository.faqList(token)
        }
    }

    private val _myTestList: MutableLiveData<Resource<MyTestListRes?>?> = MutableLiveData()
    val _myTestLiveData: MutableLiveData<Resource<MyTestListRes?>?> get() = _myTestList
    fun _myTest(token: String, examId: String) {
        viewModelScope.launch {
            _myTestList.value = Resource.Loading
            _myTestList.value = AuthRepository.myTestList(token, examId)
        }
    }

    private val _myTestContestList: MutableLiveData<Resource<TestContestListRes?>?> =
        MutableLiveData()
    val _myTestContestLiveData: MutableLiveData<Resource<TestContestListRes?>?> get() = _myTestContestList
    fun _myTestContest(token: String, testId: String) {
        viewModelScope.launch {
            _myTestContestList.value = Resource.Loading
            _myTestContestList.value = AuthRepository.myTestContestList(token, testId)
        }
    }

    private val _getContestEntryList: MutableLiveData<Resource<CommonRes?>?> = MutableLiveData()
    val _getContestEntryLiveData: MutableLiveData<Resource<CommonRes?>?> get() = _getContestEntryList
    fun _getContestEntry(token: String, params: HashMap<String, String>) {
        viewModelScope.launch {
            _getContestEntryList.value = Resource.Loading
            _getContestEntryList.value = AuthRepository.getContestEntry(token, params)
        }
    }

    private val _getMyContestList: MutableLiveData<Resource<MyTestMainFragListRes?>?> =
        MutableLiveData()
    val _getMyContestLiveData: MutableLiveData<Resource<MyTestMainFragListRes?>?> get() = _getMyContestList
    fun _getMyContest(token: String, status:String?,examcategoryId:String,testId:String?) {
        viewModelScope.launch {
            _getMyContestList.value = Resource.Loading
            _getMyContestList.value = AuthRepository.getMyContest(token, status,examcategoryId,testId)
        }
    }

    private val _getlogOut: MutableLiveData<Resource<CommonRes?>?> =
        MutableLiveData()
    val _getLogOutLiveData: MutableLiveData<Resource<CommonRes?>?> get() = _getlogOut
    fun _getLogOut(token: String) {
        viewModelScope.launch {
            _getlogOut.value = Resource.Loading
            _getlogOut.value = AuthRepository.getLogOut(token)
        }
    }

    private val _addAmount: MutableLiveData<Resource<CommonRes?>?> =
        MutableLiveData()
    val _addAmountLiveData: MutableLiveData<Resource<CommonRes?>?> get() = _addAmount
    fun _addAmount(token: String,amount:Int) {
        viewModelScope.launch {
            _addAmount.value = Resource.Loading
            _addAmount.value = AuthRepository.addAmount(token,amount)
        }
    }

    private val _withDrowAmount: MutableLiveData<Resource<CommonRes?>?> =
        MutableLiveData()
    val _withDrowAmountLiveData: MutableLiveData<Resource<CommonRes?>?> get() = _withDrowAmount
    fun withDrowAmount(token: String,amount:Int) {
        viewModelScope.launch {
            _withDrowAmount.value = Resource.Loading
            _withDrowAmount.value = AuthRepository.withDrow(token,amount)
        }
    }

    private val _getAmount: MutableLiveData<Resource<GetAmountRes?>?> =
        MutableLiveData()
    val _getAmountLiveData: MutableLiveData<Resource<GetAmountRes?>?> get() = _getAmount
    fun _getAmount(token: String) {
        viewModelScope.launch {
            _getAmount.value = Resource.Loading
            _getAmount.value = AuthRepository.getAmount(token)
        }
    }

    private val _detailsQuestion: MutableLiveData<Resource<DetailsQuestionList?>?> =
        MutableLiveData()
    val _detailsQuestionLiveData: MutableLiveData<Resource<DetailsQuestionList?>?> get() = _detailsQuestion
    fun _detailsQuestion(token: String, contestId: String) {
        viewModelScope.launch {
            _detailsQuestion.value = Resource.Loading
            _detailsQuestion.value = AuthRepository.detailsQuestion(token, contestId)
        }
    }

    private val _childLeaderBoard: MutableLiveData<Resource<ChildLeaderBoardList?>?> =
        MutableLiveData()
    val _child_leader_board_liveData: MutableLiveData<Resource<ChildLeaderBoardList?>?> get() = _childLeaderBoard
    fun _childLeaderBoardList(token: String, contestId: String) {
        viewModelScope.launch {
            _childLeaderBoard.value = Resource.Loading
            _childLeaderBoard.value = AuthRepository.childLeaderBoard(token, contestId)
        }
    }
    private val _contestResults: MutableLiveData<Resource<ResultsListRes?>?> =
        MutableLiveData()
    val _contestResultsliveData: MutableLiveData<Resource<ResultsListRes?>?> get() = _contestResults
    fun _contestResultsList(token: String, contestId: String) {
        viewModelScope.launch {
            _contestResults.value = Resource.Loading
            _contestResults.value = AuthRepository.contestResults(token, contestId)
        }
    }

    private val _practiceTestList: MutableLiveData<Resource<OngoingPracticeTestRes?>?> =
        MutableLiveData()
    val _practiceTestliveData: MutableLiveData<Resource<OngoingPracticeTestRes?>?> get() = _practiceTestList
    fun practiceTestListList(token: String,examId:String,status:String) {
        viewModelScope.launch {
            _practiceTestList.value = Resource.Loading
            _practiceTestList.value = AuthRepository.practiceTest(token, examId,status)
        }
    }
    private val _practiceCompletedTestList: MutableLiveData<Resource<CompletedPracticeTestRes?>?> =
        MutableLiveData()
    val _practiceCompletedTestliveData: MutableLiveData<Resource<CompletedPracticeTestRes?>?> get() = _practiceCompletedTestList
    fun practiceCompletedTestList(token: String,examId:String) {
        viewModelScope.launch {
            _practiceCompletedTestList.value = Resource.Loading
            _practiceCompletedTestList.value = AuthRepository.practiceCompletedTest(token, examId)
        }
    }

    private val _planList: MutableLiveData<Resource<HomePlanRes?>?> =
        MutableLiveData()
    val _planListliveData: MutableLiveData<Resource<HomePlanRes?>?> get() = _planList
    fun planHomeList(token: String,examId:String) {
        viewModelScope.launch {
            _planList.value = Resource.Loading
            _planList.value = AuthRepository.planHomeList(token, examId)
        }
    }

    private val _planAllAccessList: MutableLiveData<Resource<HomePlanRes?>?> =
        MutableLiveData()
    val _planAllAccessListliveData: MutableLiveData<Resource<HomePlanRes?>?> get() = _planAllAccessList
    fun planAllAccessHomeList(token: String,examName:String) {
        viewModelScope.launch {
            _planAllAccessList.value = Resource.Loading
            _planAllAccessList.value = AuthRepository.planAllAccessHomeList(token, examName)
        }
    }

    private val _addPlanList: MutableLiveData<Resource<AddPlanHomeRes?>?> =
        MutableLiveData()
    val _addPlanliveData: MutableLiveData<Resource<AddPlanHomeRes?>?> get() = _addPlanList
    fun addPlanHome(token: String,planId: String, subsType: String,examId: String,amount: Int,duration: String,validity: Int) {
        viewModelScope.launch {
            _addPlanList.value = Resource.Loading
            _addPlanList.value = AuthRepository.addPlanHome(token, planId,subsType,examId,amount,duration,validity)
        }
    }

    private val _contestLeaderBoardList: MutableLiveData<Resource<ContestMainLeaderBoardRes?>?> =
        MutableLiveData()
    val _contestLeaderBoardliveData: MutableLiveData<Resource<ContestMainLeaderBoardRes?>?> get() = _contestLeaderBoardList
    fun contestLeaderBoard(token: String,contestId: String) {
        viewModelScope.launch {
            _contestLeaderBoardList.value = Resource.Loading
            _contestLeaderBoardList.value = AuthRepository.contestLeaderBoard(token,contestId)
        }
    }

    private val _subscriptionSideList: MutableLiveData<Resource<SideSubscriptionRes?>?> =
        MutableLiveData()
    val _subscriptionSideliveData: MutableLiveData<Resource<SideSubscriptionRes?>?> get() = _subscriptionSideList
    fun subscriptionSide(token: String, isExpired: Boolean) {
        viewModelScope.launch {
            _subscriptionSideList.value = Resource.Loading
            _subscriptionSideList.value = AuthRepository.subScriptionSide(token, isExpired)
        }
    }

    private val _practiceDetailList: MutableLiveData<Resource<DetailPracticeTestRes?>?> =
        MutableLiveData()
    val _practiceDetailsliveData: MutableLiveData<Resource<DetailPracticeTestRes?>?> get() = _practiceDetailList
    fun practiceDetails(token: String, questionId: String) {
        viewModelScope.launch {
            _practiceDetailList.value = Resource.Loading
            _practiceDetailList.value = AuthRepository.practiceDetail(token, questionId)
        }
    }

    private val _startTestPractice: MutableLiveData<Resource<StartTestPracticeRes?>?> =
        MutableLiveData()
    val _startTestPracticeliveData: MutableLiveData<Resource<StartTestPracticeRes?>?> get() = _startTestPractice
    fun startTestPractice(token: String, praticeTestId: String, userId: String) {
        viewModelScope.launch {
            _startTestPractice.value = Resource.Loading
            _startTestPractice.value =
                AuthRepository.startTestPractice(token, praticeTestId, userId)
        }
    }

    private val _saveResponcePractice: MutableLiveData<Resource<SaveReponcePracticeRes?>?> =
        MutableLiveData()
    val _saveResponcePracticeliveData: MutableLiveData<Resource<SaveReponcePracticeRes?>?> get() = _saveResponcePractice
    fun saveResponcePractice(token: String, params: HashMap<String, String>) {
        viewModelScope.launch {
            _saveResponcePractice.value = Resource.Loading
            _saveResponcePractice.value =
                AuthRepository.saveResponcePractice(token, params)
        }
    }

    private val _resultsPractice: MutableLiveData<Resource<ResultsPracticeRes?>?> =
        MutableLiveData()
    val _resultsPracticeliveData: MutableLiveData<Resource<ResultsPracticeRes?>?> get() = _resultsPractice
    fun resultsPractice(token: String, praticeTestId: String) {
        viewModelScope.launch {
            _resultsPractice.value = Resource.Loading
            _resultsPractice.value =
                AuthRepository.resultsPractice(token, praticeTestId)
        }
    }

    private val _leaderBoardPractice: MutableLiveData<Resource<ContestMainLeaderBoardRes?>?> =
        MutableLiveData()
    val _leaderBoardPracticeliveData: MutableLiveData<Resource<ContestMainLeaderBoardRes?>?> get() = _leaderBoardPractice
    fun leaderBoardPractice(token: String, praticeTestId: String) {
        viewModelScope.launch {
            _leaderBoardPractice.value = Resource.Loading
            _leaderBoardPractice.value =
                AuthRepository.leaderBoardPractice(token, praticeTestId)
        }
    }
}