package com.example.dream_ias.apiResponse.results

data class ResultsListRes(
    var `data`: Data?,
    var message: String?,
    var status: Int?
) : java.io.Serializable {
    /* data class Data(
         var negativeMarking: Int?,
         var prize: Int?,
         var ranking: Int?,
         var result: ArrayList<Result>?,
         var totalParticipants: Int?,
         var totalScore: Int?
     ) {
         data class Result(
             var __v: Int?,
             var _id: String?,
             var answers: ArrayList<Answer>?,
             var contestId: String?,
             var createdAt: String?,
             var noOfAttempts: Int?,
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
                 var questionNo: Int?
             )
         }
     }*/
    data class Data(
        var negativeMarking: Double?,
        var prize: Int?,
        var ranking: Int?,
        var result: ArrayList<Result>?,
        var totalParticipants: Int?,
        var totalScore: Double?
    ) : java.io.Serializable {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var answers: ArrayList<Answer>?,
            var contestId: String?,
            var createdAt: String?,
            var noOfAttempts: Int?,
            var testId: String?,
            var totalIncorrect: Int?,
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
        }
    }
}