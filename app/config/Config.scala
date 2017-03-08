package config

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{AWSCredentialsProviderChain, InstanceProfileCredentialsProvider}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.kinesis.AmazonKinesisClient
import com.gu.cm.{Configuration => ConfigurationMagic, Mode}
import services.AwsInstanceTags

object Config extends AwsInstanceTags {

  val stage = readTag("Stage") getOrElse "DEV"
  val appName = readTag("App") getOrElse "atom-workshop"
  val stack = readTag("Stack") getOrElse "flexible"
  val region = services.EC2Client.region

  val awsCredentialsProvider = new AWSCredentialsProviderChain(
    new ProfileCredentialsProvider("composer"),
    new InstanceProfileCredentialsProvider(false)
  )

  //CODE uses Configuration Mode PROD so that we can use dynamo to get config. Configuration Magic currently has no CODE mode SC 15/2/17
  val configMagicMode = stage match {
    case "DEV" => Mode.Dev
    case "CODE" => Mode.Prod
    case "PROD" => Mode.Prod
    case _ => sys.error("invalid stage")
  }
  val config = ConfigurationMagic(appName, configMagicMode).load

  def getOptionalProperty[T](path: String, getVal: String => T): Option[T] = {
    if (config.hasPath(path)) Some(getVal(path))
    else None
  }

  def getPropertyIfEnabled(enabled: Boolean, path: String): String =
    if (enabled) getOptionalProperty(path, config.getString).getOrElse(sys.error(s"Property $path is required"))
    else s"feature requiring $path is disabled"

  val kinesisEnabled = getOptionalProperty("aws.kinesis.publish.enabled", config.getBoolean).getOrElse(true)

  val elkKinesisStream = getPropertyIfEnabled(kinesisEnabled, "elk.kinesis.stream")
  val elkLoggingEnabled = getOptionalProperty("elk.logging.enabled", config.getBoolean).getOrElse(true)

  val pandaDomain = config.getString("panda.domain")
  val pandaAuthCallback = config.getString("panda.authCallback")
  val pandaSystem = config.getString("panda.system")

  val dynamoDB = region.createClient(
    classOf[AmazonDynamoDBClient],
    awsCredentialsProvider,
    null
  )

  val previewDynamoTableName = config.getString("aws.dynamo.preview.tableName")
  val publishedDynamoTableName = config.getString("aws.dynamo.live.tableName")

  val gridUrl = config.getString("grid.url")

  val liveKinesisStreamName = getPropertyIfEnabled(kinesisEnabled, "aws.kinesis.publish.live")
  val previewKinesisStreamName = getPropertyIfEnabled(kinesisEnabled, "aws.kinesis.publish.preview")
  val liveReindexKinesisStreamName = getPropertyIfEnabled(kinesisEnabled, "aws.kinesis.reindex.live")
  val previewReindexKinesisStreamName = getPropertyIfEnabled(kinesisEnabled, "aws.kinesis.reindex.preview")

  val capiPreviewUrl = config.getString("capi.previewUrl")
  val capiLiveUrl = config.getString("capi.liveUrl")
  val capiUsername = config.getString("capi.previewUsername")
  val capiPassword = config.getString("capi.previewPassword")

  val kinesisClient = region.createClient(
    classOf[AmazonKinesisClient],
    awsCredentialsProvider,
    null
  )
}
