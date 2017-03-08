import com.gu.atom.play.ReindexController
import config.LogConfig
import config.Config.config
import db.AtomDataStores._
import db.AtomWorkshopDB
import db.ReindexDataStores._
import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import router.Routes

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context) with AhcWSComponents {

  val logger = new LogConfig

  lazy val router = new Routes(httpErrorHandler, appController, healthcheckController, loginController, assets, supportController, reindex)
  lazy val assets = new controllers.Assets(httpErrorHandler)
  lazy val appController = new controllers.App(wsClient, atomWorkshopDB)
  lazy val loginController = new controllers.Login(wsClient)
  lazy val healthcheckController = new controllers.Healthcheck()
  lazy val supportController = new controllers.Support(wsClient)

  lazy val reindex = new ReindexController(previewDataStore, publishedDataStore, reindexPreview, reindexPublished, Configuration(config), actorSystem)

  lazy val atomWorkshopDB = new AtomWorkshopDB()
}

