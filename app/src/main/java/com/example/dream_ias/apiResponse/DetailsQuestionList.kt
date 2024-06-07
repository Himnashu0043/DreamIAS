package com.example.dream_ias.apiResponse

data class DetailsQuestionList(
    var data: Data?,
    var message: String?,
    var status: Int?
) {
   /* data class Data(
        var contest: Contest?,
        var questions: Questions?
    ) {
        data class Contest(
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
            var quesPaperId: String?,
            var rangeAmount: ArrayList<RangeAmount>?,
            var repeatContestDaily: String?,
            var sameAstest: String?,
            var spots: Spots?,
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
                var amount: Any?,
                var higerRange: Any?,
                var lowerRange: Any?,
                var rangeChecked: String?
            )

            data class Spots(
                var openToAll: String?,
                var totalNoOfSpots: Int?
            )
        }

        data class Questions(
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
            var testQues: List<TestQue?>?,
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
                    var question: String?
                )

                data class QuestionInHin(
                    var option1: String?,
                    var option2: String?,
                    var option3: String?,
                    var option4: String?,
                    var question: String?
                )
            }
        }
    }*/
   data class Data(
       var result: ArrayList<Result>?
   ) {
       data class Result(
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
           var quesPaperId: String?,
           var questionsData: QuestionsData?,
           var rangeAmount: ArrayList<RangeAmount>?,
           var repeatContestDaily: String?,
           var sameAstest: String?,
           var spots: Spots?,
           var testId: String?,
           var testTitle: String?,
           var updatedAt: String?,
           var testData: TestData?
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
                   var timeforContest: String?,
                   var isSelected: Boolean = false,
                   var selectedPosition: Int = -1

               ) {
                   data class QuestionInEng(
                       var option1: String?,
                       var option2: String?,
                       var option3: String?,
                       var option4: String?,
                       var question: String?
                   )

                   data class QuestionInHin(
                       var option1: String?,
                       var option2: String?,
                       var option3: String?,
                       var option4: String?,
                       var question: String?
                   )
               }
           }

           data class RangeAmount(
               var _id: String?,
               var amount: Any?,
               var higerRange: Any?,
               var lowerRange: Any?,
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
       }
   }
}