package org.kravchenko.core

import java.util.concurrent.atomic.AtomicLong
import Library._

trait Library {
  type Id = Long

  def addNewBook(book: Book): Id
  def removeBookById(book: Id): Unit
  def list(): List[BookInfo]
  def search(query: SearchQuery): List[BookInfo]
  def lend(id: Id, person: String): LendOperationResult
  def getBookDetailedInfo(id: Id): BookInfoDetailed
}

class LibraryImpl extends Library {

  private var store: Map[Id, Book] = Map.empty
  private var lendInfo: Map[Id, LendInfo] = Map.empty

  private val lastId: AtomicLong = new AtomicLong(0)

  override def addNewBook(book: Book): Id = {
    val id = lastId.incrementAndGet()
    store = store.updated(id, book)
    id
  }

  override def removeBookById(book: Id): Unit = {
    this.synchronized{
      if (!lendInfo.contains(book)) {
        store = store.removed(book)
      }
    }
  }

  override def list(): List[BookInfo] = {
    list(store)
  }

  private def list(s: Map[Id, Book]): List[BookInfo] = {
    val start: Map[Book, LendInformation] = Map.empty.withDefault(_ => LendInformation(0,0))

    val result = s.foldLeft(start) { case (accum, (id, book)) =>
      val currentInfo = accum(book) //should always return because the map is 'withDefault'

      val newInfo = lendInfo.get(id) match {
        case Some(_) => currentInfo.copy(lent = currentInfo.lent + 1)
        case None    => currentInfo.copy(available = currentInfo.available + 1)
      }

      accum.updated(book, newInfo)
    }

    result.map(BookInfo.tupled).toList
  }

  override def search(query: SearchQuery): List[BookInfo] = {
    val filtered = store.filter { case (_, book) => query.checkBook(book) }
    list(filtered)
  }

  override def lend(id: Id, person: String): LendOperationResult = {
    this.synchronized {
      if (lendInfo.contains(id)) {
        LendOperationFailed
      } else {
        lendInfo = lendInfo.updated(id, LendInfo(person))
        LendOperationSucceeded
      }
    }
  }

  override def getBookDetailedInfo(id: Id): BookInfoDetailed = ???

  //used only for testing
  private[core] def getStore: List[(Id, Book)] = store.toList
  private[core] def getLendStore: List[(Id, LendInfo)] = lendInfo.toList
}

object Library {
  private[core] case class LendInfo(person: String)

  case class Book(title: String, year: Int, author: String)

  case class LendInformation(available: Long, lent: Long)
  case class BookInfo(book: Book, info: LendInformation)
  case class BookInfoDetailed(book: Book, lent: Boolean, person: Option[String])

  sealed trait LendOperationResult
  case object LendOperationSucceeded extends LendOperationResult
  case object LendOperationFailed extends LendOperationResult

  case class SearchQuery(title: Option[String] = None, year: Option[Int] = None, author: Option[String] = None) {
    def checkBook(book: Book): Boolean = {
      title.map(book.title == _).getOrElse(true) &&
        year.map(book.year == _).getOrElse(true) &&
          author.map(book.author == _).getOrElse(true)
    }
  }
}