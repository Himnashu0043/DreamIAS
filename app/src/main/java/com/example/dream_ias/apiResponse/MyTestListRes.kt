package com.example.dream_ias.apiResponse

data class MyTestListRes(
    var data: Data?,
    var message: String?,
    var status: Int?
) {
    /*data class Data(
        var result: ArrayList<Result>?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var addedId: String?,
            var configureGuide: ArrayList<ConfigureGuide>?,
            var conteststartTime: String?,
            var createdAt: String?,
            var description: String?,
            var duration: String?,
            var examId: String?,
            var instructions: List<String?>?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var negativeMarks: Int?,
            var questions: Int?,
            var repeat: String?,
            var testNo: String?,
            var testStatus: String?,
            var testTitle: String?,
            var teststartTime: String?,
            var testsubTitle: String?,
            var updatedAt: String?
        ) {
            data class ConfigureGuide(
                var _id: String?,
                var paragraph: String?,
                var title: String?
            )
        }
    }*/
   /* data class Data(
        var result: ArrayList<Result>?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var addedId: String?,
            var configureGuide: ArrayList<ConfigureGuide>?,
            var conteststartTime: String?,
            var createdAt: String?,
            var duration: String?,
            var examId: String?,
            var instructions: String?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var questions: Int?,
            var repeat: String?,
            var startDate: String?,
            var testNo: String?,
            var testStatus: String?,
            var testTitle: String?,
            var teststartTime: String?,
            var testsubTitle: String?,
            var totalMarks: Int?,
            var updatedAt: String?
        ) {
            data class ConfigureGuide(
                var _id: String?,
                var paragraph: String?
            )
        }
    }*/
   /* data class Data(
        var result: ArrayList<Result>?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var addedId: String?,
            var configureGuide: ArrayList<ConfigureGuide>?,
            var conteststartTime: String?,
            var createdAt: String?,
            var duration: String?,
            var examId: String?,
            var instructions: String?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var questions: Int?,
            var repeat: String?,
            var testNo: String?,
            var testStatus: String?,
            var testTitle: String?,
            var teststartTime: String?,
            var testsubTitle: String?,
            var totalMarks: Any?,
            var updatedAt: String?
        ) {
            data class ConfigureGuide(
                var _id: String?,
                var paragraph: String?
            )
        }
    }*/
   /* data class Data(
        var result: ArrayList<Result>?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var addedId: String?,
            var createdAt: String?,
            var date: String?,
            var examId: String?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var negativeMarking: String?,
            var questionNo: String?,
            var solution: Solution?,
            var testData: TestData?,
            var testId: String?,
            var testQues: ArrayList<TestQue>?,
            var updatedAt: String?
        ) {
            data class Solution(
                var link: String?,
                var pdf: String?,
                var solAvailable: String?
            )

            data class TestData(
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var configureGuide: ArrayList<ConfigureGuide>?,
                var conteststartTime: String?,
                var createdAt: String?,
                var duration: String?,
                var examId: String?,
                var instructions: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var negativeMarks: Int?,
                var questions: Int?,
                var repeat: String?,
                var startDate: String?,
                var testNo: String?,
                var testStatus: String?,
                var testTitle: String?,
                var teststartTime: String?,
                var testsubTitle: String?,
                var updatedAt: String?
            ) {
                data class ConfigureGuide(
                    var _id: String?,
                    var paragraph: String?
                )
            }

            data class TestQue(
                var _id: String?,
                var correctOption: String?,
                var marks: Int?,
                var questionInEng: ArrayList<QuestionInEng>?,
                var questionInHin: ArrayList<QuestionInHin>?,
                var timeforContest: String?
            ) {
                data class QuestionInEng(
                    var _id: String?,
                    var option: String?,
                    var question: String?
                )

                data class QuestionInHin(
                    var _id: String?,
                    var option: String?,
                    var question: String?
                )
            }
        }
    }*/
   /* data class Data(
        var mycontest: ArrayList<Mycontest>?,
        var upComingContest: ArrayList<UpComingContest>?,
        var upComingPractice: ArrayList<UpComingPractice>?
    ) {
        data class Mycontest(
            var __v: Int?,
            var _id: String?,
            var amount: Int?,
            var contestId: String?,
            var createdAt: String?,
            var examId: String?,
            var paymentStatus: String?,
            var questionId: String?,
            var status: String?,
            var testData: TestData?,
            var testId: String?,
            var updatedAt: String?,
            var userId: String?
        ) {
            data class TestData(
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var configureGuide: ArrayList<ConfigureGuide>?,
                var conteststartTime: String?,
                var createdAt: String?,
                var duration: String?,
                var examId: String?,
                var instructions: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var questions: Int?,
                var repeat: String?,
                var startDate: String?,
                var testNo: String?,
                var testStatus: String?,
                var testTitle: String?,
                var teststartTime: String?,
                var testsubTitle: String?,
                var totalMarks: Int?,
                var updatedAt: String?
            ) {
                data class ConfigureGuide(
                    var _id: String?,
                    var paragraph: String?
                )
            }
        }

        data class UpComingContest(
            var __v: Int?,
            var _id: String?,
            var addedId: String?,
            var createdAt: String?,
            var date: String?,
            var examId: String?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var negativeMarking: String?,
            var questionNo: String?,
            var solution: Solution?,
            var testData: TestData?,
            var testId: String?,
            var testQues: ArrayList<TestQue>?,
            var updatedAt: String?
        ) {
            data class Solution(
                var link: String?,
                var pdf: String?,
                var solAvailable: String?
            )

            data class TestData(
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var configureGuide: ArrayList<ConfigureGuide>?,
                var conteststartTime: String?,
                var createdAt: String?,
                var duration: String?,
                var examId: String?,
                var instructions: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var negativeMarks: Int?,
                var questions: Int?,
                var repeat: String?,
                var startDate: String?,
                var testNo: String?,
                var testStatus: String?,
                var testTitle: String?,
                var teststartTime: String?,
                var testsubTitle: String?,
                var updatedAt: String?
            ) {
                data class ConfigureGuide(
                    var _id: String?,
                    var paragraph: String?
                )
            }

            data class TestQue(
                var _id: String?,
                var correctOption: String?,
                var marks: Int?,
                var questionInEng: ArrayList<QuestionInEng>?,
                var questionInHin: ArrayList<QuestionInHin>?,
                var timeforContest: String?
            ) {
                data class QuestionInEng(
                    var _id: String?,
                    var option: String?,
                    var question: String?
                )

                data class QuestionInHin(
                    var _id: String?,
                    var option: String?,
                    var question: String?
                )
            }
        }

        data class UpComingPractice(
            var __v: Int?,
            var _id: String?,
            var amount: Int?,
            var contestId: String?,
            var createdAt: String?,
            var examId: String?,
            var paymentStatus: String?,
            var questionId: String?,
            var status: String?,
            var testData: TestData?,
            var testId: String?,
            var updatedAt: String?,
            var userId: String?
        ) {
            data class TestData(
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var configureGuide: ArrayList<ConfigureGuide>?,
                var conteststartTime: String?,
                var createdAt: String?,
                var duration: String?,
                var examId: String?,
                var instructions: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var questions: Int?,
                var repeat: String?,
                var startDate: String?,
                var testNo: String?,
                var testStatus: String?,
                var testTitle: String?,
                var teststartTime: String?,
                var testsubTitle: String?,
                var totalMarks: Int?,
                var updatedAt: String?
            ) {
                data class ConfigureGuide(
                    var _id: String?,
                    var paragraph: String?
                )
            }
        }
    }*/




    data class Data(
        var mycontest: ArrayList<Mycontest>?,
        var subscribed: Boolean?,
        var upComingContest: ArrayList<UpComingContest>?,
        var upComingPractice: ArrayList<UpComingPractice>?
    ) {
        /*data class Mycontest(
            var __v: Int?,
            var _id: String?,
            var amount: Int?,
            var conData: ConData?,
            var contestId: String?,
            var createdAt: String?,
            var examId: String?,
            var paymentStatus: String?,
            var questionId: String?,
            var status: String?,
            var testData: TestData?,
            var testId: String?,
            var updatedAt: String?,
            var userId: String?
        ) {
            data class ConData(
                var Guide: List<Any?>?,
                var Instructions: String?,
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var contestCount: Int?,
                var contestNo: String?,
                var createdAt: String?,
                var crosscutOff: ArrayList<CrosscutOff>?,
                var cutofMarks: Any?,
                var dreamCandidate: String?,
                var dreamCandidateAmount: Int?,
                var entryFee: EntryFee?,
                var examId: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var liveStreaming: String?,
                var liveStreamingLink: String?,
                var liveStreamingTime: String?,
                var maxRank: Int?,
                var minRank: Int?,
                var prizeAmount: String?,
                var prizeType: String?,
                var rangeAmount: ArrayList<RangeAmount>?,
                var repeatContestDaily: String?,
                var sameAstest: String?,
                var spots: Spots?,
                var studentEntrySpotsNo: Int?,
                var testId: String?,
                var testTitle: String?,
                var updatedAt: String?
            ) {
                data class CrosscutOff(
                    var _id: String?,
                    var candidateAmount: Any?,
                    var cutoffMarks: Any?,
                    var prizeAmount: Any?
                )

                data class EntryFee(
                    var discount: Int?,
                    var entryFee: Int?,
                    var noEntryFee: String?
                )

                data class RangeAmount(
                    var _id: String?,
                    var amount: Int?,
                    var higerRange: Int?,
                    var lowerRange: Int?,
                    var rangeChecked: String?
                )

                data class Spots(
                    var openToAll: String?,
                    var totalNoOfSpots: Int?
                )
            }

            data class TestData(
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var configureGuide: List<ConfigureGuide?>?,
                var contestStatus: String?,
                var conteststartTime: String?,
                var createdAt: String?,
                var duration: String?,
                var examId: String?,
                var instructions: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var practiceStatus: String?,
                var questions: Int?,
                var repeat: String?,
                var startDate: String?,
                var testNo: String?,
                var testTitle: String?,
                var teststartTime: String?,
                var testsubTitle: String?,
                var totalMarks: Int?,
                var updatedAt: String?
            ) {
                data class ConfigureGuide(
                    var _id: String?,
                    var paragraph: String?,
                    var title: String?
                )
            }
        }*/
        data class Mycontest(
            var __v: Int?,
            var _id: String?,
            var amount: Int?,
            var answers: ArrayList<Answer>?,
            var conData: ConData?,
            var contestId: String?,
            var createdAt: String?,
            var examId: String?,
            var isParticipate: Boolean?,
            var noOfAttempts: Int?,
            var participationId: String?,
            var paymentStatus: String?,
            var questionId: String?,
            var status: String?,
            var testData: TestData?,
            var testId: String?,
            var totalIncorrect: Int?,
            var totalcorrectAnswers: Int?,
            var updatedAt: String?,
            var userId: String?
        ) {
            data class Answer(
                var _id: String?,
                var ans: String?,
                var iscorrect: String?,
                var marks: Int?,
                var queAns: String?,
                var question: String?,
                var questionNo: Int?
            )

            data class ConData(
                var Guide: List<Any?>?,
                var Instructions: String?,
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var contestCount: Int?,
                var contestNo: String?,
                var createdAt: String?,
                var crosscutOff: ArrayList<CrosscutOff>?,
                var cutofMarks: Int?,
                var dreamCandidate: String?,
                var dreamCandidateAmount: Int?,
                var entryFee: EntryFee?,
                var examId: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var liveStreaming: String?,
                var liveStreamingLink: String?,
                var liveStreamingTime: String?,
                var maxRank: Int?,
                var minRank: Int?,
                var prizeAmount: String?,
                var prizeType: String?,
                var rangeAmount: ArrayList<RangeAmount>?,
                var repeatContestDaily: String?,
                var sameAstest: String?,
                var spots: Spots?,
                var studentEntrySpotsNo: Int?,
                var testId: String?,
                var testTitle: String?,
                var updatedAt: String?
            ) {
                data class CrosscutOff(
                    var _id: String?,
                    var candidateAmount: Any?,
                    var cutoffMarks: Any?,
                    var prizeAmount: Any?
                )

                data class EntryFee(
                    var discount: Int?,
                    var entryFee: Int?,
                    var noEntryFee: String?
                )

                data class RangeAmount(
                    var _id: String?,
                    var amount: Int?,
                    var higerRange: Int?,
                    var lowerRange: Int?,
                    var rangeChecked: String?
                )

                data class Spots(
                    var openToAll: String?,
                    var totalNoOfSpots: Int?
                )
            }

            data class TestData(
                var __v: Int?,
                var _id: String?,
                var addedContest: Int?,
                var addedId: String?,
                var configureGuide: ArrayList<ConfigureGuide>?,
                var contestIsadded: Boolean?,
                var contestStatus: String?,
                var conteststartTime: String?,
                var createdAt: String?,
                var duration: String?,
                var examId: String?,
                var instructions: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var practiceStatus: String?,
                var questions: Int?,
                var repeat: String?,
                var startDate: String?,
                var testNo: String?,
                var testTitle: String?,
                var teststartTime: String?,
                var testsubTitle: String?,
                var totalMarks: Int?,
                var updatedAt: String?
            ) {
                data class ConfigureGuide(
                    var _id: String?,
                    var paragraph: String?,
                    var title: String?
                )
            }
        }
        data class UpComingPractice(
            var __v: Int?,
            var _id: String?,
            var amount: Int?,
            var contestId: String?,
            var createdAt: String?,
            var examId: String?,
            var paymentStatus: String?,
            var questionId: String?,
            var status: String?,
            var testData: TestData?,
            var testId: String?,
            var updatedAt: String?,
            var userId: String?
        ) {
            data class TestData(
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var configureGuide: ArrayList<ConfigureGuide>?,
                var conteststartTime: String?,
                var createdAt: String?,
                var duration: String?,
                var examId: String?,
                var instructions: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var questions: Int?,
                var repeat: String?,
                var startDate: String?,
                var testNo: String?,
                var testStatus: String?,
                var testTitle: String?,
                var teststartTime: String?,
                var testsubTitle: String?,
                var totalMarks: Int?,
                var updatedAt: String?
            ) {
                data class ConfigureGuide(
                    var _id: String?,
                    var paragraph: String?
                )
            }
        }

       /* data class UpComingContest(
            var Guide: List<Any?>?,
            var Instructions: String?,
            var __v: Int?,
            var _id: String?,
            var addedId: String?,
            var contestCount: Int?,
            var contestNo: String?,
            var createdAt: String?,
            var crosscutOff: List<CrosscutOff?>?,
            var cutofMarks: Any?,
            var dreamCandidate: String?,
            var dreamCandidateAmount: Int?,
            var entryFee: EntryFee?,
            var examId: String?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var liveStreaming: String?,
            var liveStreamingLink: String?,
            var liveStreamingTime: String?,
            var maxRank: Int?,
            var minRank: Int?,
            var prizeAmount: String?,
            var prizeType: String?,
            var rangeAmount: List<RangeAmount?>?,
            var repeatContestDaily: String?,
            var sameAstest: String?,
            var spots: Spots?,
            var studentEntrySpotsNo: Any?,
            var testData: TestData?,
            var testId: String?,
            var testTitle: String?,
            var updatedAt: String?,
            var remainingTime:String = ""
        ) {
            data class CrosscutOff(
                var _id: String?,
                var candidateAmount: Any?,
                var cutoffMarks: Any?,
                var prizeAmount: Any?
            )

            data class EntryFee(
                var discount: Int?,
                var entryFee: Int?,
                var noEntryFee: String?
            )

            data class RangeAmount(
                var _id: String?,
                var amount: Int?,
                var higerRange: Int?,
                var lowerRange: Int?,
                var rangeChecked: String?
            )

            data class Spots(
                var openToAll: String?,
                var totalNoOfSpots: Int?
            )

            data class TestData(
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var configureGuide: List<ConfigureGuide?>?,
                var contestStatus: String?,
                var conteststartTime: String?,
                var createdAt: String?,
                var duration: String?,
                var examId: String?,
                var instructions: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var practiceStatus: String?,
                var questions: Int?,
                var repeat: String?,
                var startDate: String?,
                var testNo: String?,
                var testTitle: String?,
                var teststartTime: String?,
                var testsubTitle: String?,
                var totalMarks: Int?,
                var updatedAt: String?
            ) {
                data class ConfigureGuide(
                    var _id: String?,
                    var paragraph: String?,
                    var title: String?
                )
            }
        }*/
       data class UpComingContest(
           var __v: Int?,
           var _id: String?,
           var addedContest: Int?,
           var addedId: String?,
           var configureGuide: ArrayList<ConfigureGuide>?,
           var contestData: ArrayList<ContestData>?,
           var contestIsadded: Boolean?,
           var contestStatus: String?,
           var conteststartTime: String?,
           var createdAt: String?,
           var duration: String?,
           var examId: String?,
           var instructions: String?,
           var isActive: Boolean?,
           var isDeleted: Boolean?,
           var practiceStatus: String?,
           var questions: Int?,
           var repeat: String?,
           var startDate: String?,
           var testNo: String?,
           var testTitle: String?,
           var teststartTime: String?,
           var testsubTitle: String?,
           var totalMarks: Int?,
           var updatedAt: String?
       ) {
           data class ConfigureGuide(
               var _id: String?,
               var paragraph: String?,
               var title: String?
           )

           data class ContestData(
               var Guide: List<Any?>?,
               var Instructions: String?,
               var __v: Int?,
               var _id: String?,
               var addedId: String?,
               var contestCount: Int?,
               var contestNo: String?,
               var createdAt: String?,
               var crosscutOff: List<CrosscutOff?>?,
               var cutofMarks: Int?,
               var dreamCandidate: String?,
               var dreamCandidateAmount: Int?,
               var entryFee: EntryFee?,
               var examId: String?,
               var isActive: Boolean?,
               var isDeleted: Boolean?,
               var liveStreaming: String?,
               var liveStreamingLink: String?,
               var liveStreamingTime: String?,
               var maxRank: Int?,
               var minRank: Int?,
               var prizeAmount: String?,
               var prizeType: String?,
               var rangeAmount: ArrayList<RangeAmount>?,
               var repeatContestDaily: String?,
               var sameAstest: String?,
               var spots: Spots?,
               var studentEntrySpotsNo: Any?,
               var testId: String?,
               var testTitle: String?,
               var updatedAt: String?
           ) {
               data class CrosscutOff(
                   var _id: String?,
                   var candidateAmount: Any?,
                   var cutoffMarks: Int?,
                   var prizeAmount: Int?
               )

               data class EntryFee(
                   var discount: Int?,
                   var entryFee: Int?,
                   var noEntryFee: String?
               )

               data class RangeAmount(
                   var _id: String?,
                   var amount: Int?,
                   var higerRange: Int?,
                   var lowerRange: Int?,
                   var rangeChecked: String?
               )

               data class Spots(
                   var openToAll: String?,
                   var totalNoOfSpots: Int?
               )
           }
       }
    }
}