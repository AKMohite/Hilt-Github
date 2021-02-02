package com.ak.githilt.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RemoteKeysDao: BaseDao<RemoteKeysEntity> {


    @Query("SELECT * FROM remote_keys WHERE id = :repoId")
    suspend fun remoteKeysRepoId(repoId: Long): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

}