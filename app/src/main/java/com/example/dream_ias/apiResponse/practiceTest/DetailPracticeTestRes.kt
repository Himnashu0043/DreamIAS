package com.example.dream_ias.apiResponse.practiceTest

data class DetailPracticeTestRes(
    var data: Data?,
    var message: String?,
    var status: Int?
) {
    data class Data(
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
            var negativeMarking: NegativeMarking?,
            var questionNo: String?,
            var solution: Solution?,
            var testData: TestData?,
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
                    var questionNoHin: Int?
                )
            }
        }
    }
}