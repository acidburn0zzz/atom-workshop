package db

import com.gu.contentatom.thrift.{Atom, AtomType}
import com.gu.atom.data.{DynamoDataStore, DataStoreResult, IDNotFound}
import play.api.Logger
import cats.syntax.either._
import models.{AtomAPIError, AtomWorkshopDynamoDatastoreError}
import com.gu.pandomainauth.model.User
import util.AtomLogic._
import AtomDataStores._

trait AtomWorkshopDBAPI {

  def transformAtomLibResult[T](result: DataStoreResult.DataStoreResult[T]): Either[AtomAPIError, T] = result match {
    case Left(e) => Left(AtomWorkshopDynamoDatastoreError(e.msg))
    case Right(r) => Right(r)
  }

  def createAtom(datastore: DynamoDataStore, atomType: AtomType, user: User, atom: Atom): Either[AtomAPIError, Atom]


  def getAtom(datastore: DynamoDataStore, atomType: AtomType, id: String): Either[AtomAPIError, Atom]

  def publishAtom(datastore: DynamoDataStore, user: User, newVersion: Atom): Either[AtomAPIError, Atom]

  def updateAtom(datastore: DynamoDataStore, atom: Atom): Either[AtomAPIError, Atom]

  def deleteAtom(datastore: DynamoDataStore, atomType: AtomType, id: String): Either[AtomAPIError, Atom]
}

class AtomWorkshopDB() extends AtomWorkshopDBAPI {

  def createAtom(datastore: DynamoDataStore, atomType: AtomType, user: User, atom: Atom): Either[AtomAPIError, Atom] = {
    Logger.info(s"Attempting to create atom of type ${atomType.name} with id ${atom.id}")
    try {
      val result = datastore.createAtom(buildKey(atomType, atom.id), atom)
      Logger.info(s"Successfully created atom of type ${atomType.name} with id ${atom.id}")
      transformAtomLibResult(result)
    } catch {
      case e: Exception => processException(e)
    }
  }

  def getAtom(datastore: DynamoDataStore, atomType: AtomType, id: String) =
    transformAtomLibResult(datastore.getAtom(buildKey(atomType, id)))

  def updateAtom(datastore: DynamoDataStore, atom: Atom): Either[AtomAPIError, Atom] = {
    try {
      val result = datastore.updateAtom(atom)
      Logger.info(s"Successfully updated atom of type ${atom.atomType.name} with id ${atom.id}")
      transformAtomLibResult(result)
    } catch {
      case e: Exception => processException(e)
    }
  }

  def publishAtom(datastore: DynamoDataStore, user: User, newAtom: Atom): Either[AtomAPIError, Atom] = {

    def checkAtomExistsInDatastore(datastore: DynamoDataStore, atomType: AtomType, id: String): Either[AtomAPIError, Boolean] =
      datastore.getAtom(buildKey(atomType, id)).fold({
        case IDNotFound => Right(false)
        case e => Left(AtomWorkshopDynamoDatastoreError(e.msg))
      }, _ => Right(true))

    checkAtomExistsInDatastore(datastore, newAtom.atomType, newAtom.id).fold(err => Left(err), result => {
      if (result) updateAtom(datastore, newAtom)
      else createAtom(datastore, newAtom.atomType, user, newAtom)

    })
  }

  def deleteAtom(datastore: DynamoDataStore, atomType: AtomType, id: String) =
    transformAtomLibResult(datastore.deleteAtom(buildKey(atomType, id)))
}