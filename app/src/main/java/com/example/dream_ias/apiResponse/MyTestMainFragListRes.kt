package com.example.dream_ias.apiResponse

data class MyTestMainFragListRes(
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
        }
    }*/
    /*data class Data(
        var result: ArrayList<Result>?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var amount: Int?,
            var contestData: ContestData?,
            var contestId: String?,
            var createdAt: String?,
            var examId: String?,
            var paymentStatus: String?,
            var questionId: String?,
            var status: String?,
            var testData: TestData?,
            var testId: String?,
            var updatedAt: String?,
            var userId: String?,
            var conteststartTime: String?
        ) {
            data class ContestData(
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
                var configureGuide: ArrayList<ConfigureGuide>?,
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
    }*/

    data class Data(
        var result: ArrayList<Result>?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var amount: Int?,
            var answers: ArrayList<Any?>?,
            var contestData: ContestData?,
            var contestId: String?,
            var createdAt: String?,
            var examId: String?,
            var isParticipate: Boolean?,
            var isPrizeWinning: Boolean?,
            var paymentStatus: String?,
            var questionId: String?,
            var questionsData: QuestionsData?,
            var status: String?,
            var testData: TestData?,
            var testId: String?,
            var updatedAt: String?,
            var userId: String?
        ) {
            data class ContestData(
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
                var maxRank: Any?,
                var minRank: Any?,
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

            data class QuestionsData(
                var __v: Int?,
                var _id: String?,
                var addedId: String?,
                var createdAt: String?,
                var date: String?,
                var examId: String?,
                var isActive: Boolean?,
                var isDeleted: Boolean?,
                var negativeMarking: NegativeMarking?,
                var questionNo: String?,
                var solution: Solution?,
                var testId: String?,
                var testQues: ArrayList<TestQue>?,
                var updatedAt: String?
            ) {
                data class NegativeMarking(
                    var marking: Int?,
                    var markingStatus: String?
                )

                data class Solution(
                    var link: String?,
                    var pdf: String?,
                    var solAvailable: String?
                )

                data class TestQue(
                    var _id: String?,
                    var correctOption: String?,
                    var marks: Int?,
                    var questionInEng: QuestionInEng?,
                    var questionInHin: QuestionInHin?,
                    var timeforContest: String?
                ) {
                    data class QuestionInEng(
                        var option1: String?,
                        var option2: String?,
                        var option3: String?,
                        var option4: String?,
                        var question: String?,
                        var questionNoEng: Int?
                    )

                    data class QuestionInHin(
                        var option1: String?,
                        var option2: String?,
                        var option3: String?,
                        var option4: String?,
                        var question: String?,
                        var questionNoHin: Any?
                    )
                }
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
    }
}