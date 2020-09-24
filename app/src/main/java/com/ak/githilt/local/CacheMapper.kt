package com.ak.githilt.local

import com.ak.githilt.model.Repo
import com.ak.githilt.util.EntityMapper
import javax.inject.Inject

class CacheMapper @Inject constructor(): EntityMapper<RepoCacheEntity, Repo> {
    override fun mapFromEntity(entity: RepoCacheEntity): Repo {
        return Repo(
            id = entity.id,
            name = entity.name,
            fullName = entity.fullName,
            repoDescription = entity.repoDescription,
            repoUrl = entity.repoUrl,
            starsCount = entity.starsCount,
            forksCount = entity.forksCount,
            language = entity.language

        )
    }

    override fun mapToEntity(domainModel: Repo): RepoCacheEntity {
        return RepoCacheEntity(
            id = domainModel.id,
            name = domainModel.name,
            fullName = domainModel.fullName,
            repoDescription = domainModel.repoDescription,
            repoUrl = domainModel.repoUrl,
            starsCount = domainModel.starsCount,
            forksCount = domainModel.forksCount,
            language = domainModel.language
        )
    }

    fun mapFromEntityList(entities: List<RepoCacheEntity>): List<Repo>{
        return entities.map { entity -> mapFromEntity(entity) }
    }
}