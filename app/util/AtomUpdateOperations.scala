package util

import com.gu.contentatom.thrift.Atom
import com.gu.fezziwig.CirceScroogeMacros._
import com.gu.pandomainauth.model.User
import io.circe.Json
import io.circe.syntax._
import models.AtomAPIError
import util.AtomElementBuilders._
import util.Parser._

object AtomUpdateOperations {
  def updateTopLevelFields(atom: Atom, user: User, publish: Boolean = false): Atom =
    atom.copy(
      contentChangeDetails = buildContentChangeDetails(user, Some(atom.contentChangeDetails), updateLastModified = true, updatePublished = publish),
      defaultHtml = buildDefaultHtml(atom.atomType, atom.data)
    )

  def updateAtomFromJson(atom: Atom, json: Json, user: User): Either[AtomAPIError, Atom] = jsonToAtom(atom.asJson.deepMerge(json))

}