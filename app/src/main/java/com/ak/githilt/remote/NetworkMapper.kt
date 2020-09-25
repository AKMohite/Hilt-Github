package com.ak.githilt.remote

import com.ak.githilt.model.Repo
import com.ak.githilt.util.EntityMapper
import javax.inject.Inject

class NetworkMapper @Inject constructor(): EntityMapper<RepoInfo, Repo> {

    override fun mapFromEntity(entity: RepoInfo): Repo {

        return Repo(
            id = entity.id,
            name = entity.name,
            fullName = entity.fullName,
            repoDescription = entity.description,
            repoUrl = entity.url,
            starsCount = entity.stars,
            forksCount = entity.forks,
            language = entity.language,
            page = entity.page
        )
    }

    override fun mapToEntity(domainModel: Repo): RepoInfo {
        return RepoInfo(
            id = domainModel.id,
            name = domainModel.name,
            fullName = domainModel.fullName,
            description = domainModel.repoDescription,
            url = domainModel.repoUrl,
            stars = domainModel.starsCount,
            forks = domainModel.forksCount,
            language = domainModel.language,
            page = domainModel.page
        )
    }

    fun mapFromEntityList(entities: List<RepoInfo>): List<Repo>{
        return entities.map { entity -> mapFromEntity(entity) }
    }
}