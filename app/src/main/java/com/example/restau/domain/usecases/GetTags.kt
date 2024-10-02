package com.example.restau.domain.usecases

import com.example.restau.domain.repository.TagsRepository

class GetTags (private val tagsRepository: TagsRepository){
    suspend operator fun invoke(): Map<String, Pair<Int, List<String>>> {
        return tagsRepository.getTags()
    }
}