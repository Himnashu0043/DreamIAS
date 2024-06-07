package com.example.dream_ias.apiResponse.practiceTest

data class ResultsPracticeRes(
    var data: Data?,
    var message: String?,
    var status: Int?
) : java.io.Serializable {
    /*data class Data(
        var negativeMarking: Double?,
        var result: List<Result?>?,
        var totalParticipants: Int?,
        var totalScore: Double?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var answers: List<Answer?>?,
            var createdAt: String?,
            var isPrizeWinning: Boolean?,
            var noOfAttempts: Int?,
            var praticeTestId: String?,
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
        }
    }*/
    data class Data(
        var negativeMarking: Double?,
        var ranking: Int?,
        var result: List<Result?>?,
        var totalParticipants: Int?,
        var totalScore: Double?
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var answers: ArrayList<Answer>?,
            var createdAt: String?,
            var isPrizeWinning: Boolean?,
            var noOfAttempts: Int?,
            var praticeTestId: String?,
            var solutionData: SolutionData?,
            var totalIncorrect: Int?,
            var totalScore: Double?,
            var totalcorrectAnswers: Int?,
            var updatedAt: String?,
            var userId: String?
        ) : java.io.Serializable {
            data class Answer(
                var _id: String?,
                var ans: String?,
                var iscorrect: String?,
                var marks: Int?,
                var queAns: String?,
                var question: String?,
                var questionNo: Int?
            ) : java.io.Serializable

            data class SolutionData(
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
            ) : java.io.Serializable {
                data class NegativeMarking(
                    var marking: Int?,
                    var markingStatus: String?
                ) : java.io.Serializable

                data class Solution(
                    var link: String?,
                    var pdf: String?,
                    var solAvailable: String?
                ) : java.io.Serializable

                data class TestQue(
                    var _id: String?,
                    var correctOption: String?,
                    var marks: Int?,
                    var questionInEng: QuestionInEng?,
                    var questionInHin: QuestionInHin?,
                    var timeforContest: String?
                ) : java.io.Serializable {
                    data class QuestionInEng(
                        var option1: String?,
                        var option2: String?,
                        var option3: String?,
                        var option4: String?,
                        var question: String?,
                        var questionNoEng: Int?
                    ) : java.io.Serializable

                    data class QuestionInHin(
                        var option1: String?,
                        var option2: String?,
                        var option3: String?,
                        var option4: String?,
                        var question: String?,
                        var questionNoHin: Any?
                    ) : java.io.Serializable
                }
            }
        }
    }
}