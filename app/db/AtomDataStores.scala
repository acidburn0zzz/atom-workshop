package db

import com.gu.atom.data._
import com.gu.atom.publish.{PreviewKinesisAtomReindexer, PublishedKinesisAtomReindexer}
import config.Config._
import models.{Live, Preview, Version}

object AtomDataStores {
  def getDataStore(version: Version): DynamoDataStore = version match {
    case Live => publishedDataStore
    case Preview => previewDataStore
  }

  val previewDataStore = new PreviewDynamoDataStore(dynamoDB, previewDynamoTableName)
  val publishedDataStore = new PublishedDynamoDataStore(dynamoDB, previewDynamoTableName)
}

object ReindexDataStores {
  val reindexPreview: PreviewKinesisAtomReindexer =
    new PreviewKinesisAtomReindexer(previewReindexKinesisStreamName, kinesisClient)

  val reindexPublished: PublishedKinesisAtomReindexer =
    new PublishedKinesisAtomReindexer(liveReindexKinesisStreamName, kinesisClient)
}