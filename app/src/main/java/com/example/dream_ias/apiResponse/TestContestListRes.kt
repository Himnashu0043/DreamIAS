package com.example.dream_ias.apiResponse

data class TestContestListRes(
    var `data`: Data?,
    var message: String?,
    var status: Int?
):java.io.Serializable {
    data class Data(
        var result: ArrayList<Result>?
    ):java.io.Serializable {
        data class Result(
            var Guide1: ArrayList<Guide>?,
            var Instructions: String?,
            var __v: Int?,
            var _id: String?,
            var addedId: String?,
            var configureGuide: ArrayList<ConfigureGuide>?,
            var contestCount: Int?,
            var contestNo: String?,
            var contestStartTime: String?,
            var contestStatus: String?,
            var createdAt: String?,
            var crosscutOff: ArrayList<CrosscutOff>?,
            var cutofMarks: Int?,
            var dreamCandidateAmount: Int?,
            var entryFee: EntryFee?,
            var examCategory: String?,
            var examId: String?,
            var instruction: String?,
            var isActive: Boolean?,
            var isDeleted: Boolean?,
            var liveStreaming: String?,
            var liveStreamingLink: String?,
            var maxRank: Int?,
            var minRank: Int?,
            var prizeAmount: String?,
            var prizeType: String?,
            var quesPaperId: String?,
            var rangeAmount: ArrayList<RangeAmount>?,
            var repeatContestDaily: String?,
            var spots: Spots?,
            var testDuration: String?,
            var testId: String?,
            var testStartTime: Int?,
            var testSubTitle: String?,
            var testTitle: String?,
            var totalMarks: String?,
            var totalQuestion: String?,
            var questions: Int?,
            var duration: String?,
            var updatedAt: String?,
            var studentEntrySpotsNo: Int?,
            var hasUserEntered: Boolean,

        ):java.io.Serializable {
            data class Guide(
                var _id: String?
            ) : java.io.Serializable

            data class ConfigureGuide(
                var _id: String?,
                var paragraph: String?,
                var title: String?
            ) : java.io.Serializable

            data class CrosscutOff(
                var _id: String?,
                var candidateAmount: Int?,
                var cutoffMarks: Int?,
                var prizeAmount: Int?
            ) : java.io.Serializable

            data class EntryFee(
                var discount: Int?,
                var entryFee: Int?,
                var noEntryFee: String?
            ):java.io.Serializable

            data class RangeAmount(
                var _id: String?,
                var amount: Int?,
                var higerRange: Int?,
                var lowerRange: Int?
            ):java.io.Serializable

            data class Spots(
                var openToAll: String?,
                var totalNoOfSpots: Int?
            ):java.io.Serializable
        }
    }
}