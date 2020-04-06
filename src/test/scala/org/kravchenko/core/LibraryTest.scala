package org.kravchenko.core

import org.kravchenko.core.Library.{Book, BookInfo, BookInfoDetailed, LendInfo, LendInformation, SearchQuery}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import LibraryTest._

class LibraryTest extends AnyFlatSpec with Matchers {

  "A library" should "add one new book" in new TestLibrary {
    //When
    library.addNewBook(book1)

    //Then
    library.getStore must have size 1
    library.getStore must contain ((1, book1))
  }

  it should "add multiple copies of one book" in new TestLibrary {
    //When
    library.addNewBook(book1)
    library.addNewBook(book1)

    //Then
    libraryStore must have size 2
    libraryStore must contain allOf ((1, book1), (2, book1))
  }

  it should "add multiple books" in new TestLibrary {
    //When
    library.addNewBook(book1)
    library.addNewBook(book2)
    library.addNewBook(book3)

    //Then
    libraryStore must have size 3
    libraryStore must contain allOf ((1, book1), (2, book2), (3, book3))
  }

  it should "delete existing book from the library" in new TestLibrary {
    //Given
    library.addNewBook(book1)

    //When
    library.removeBookById(1L)

    //Then
    libraryStore must have size 0
  }

  it should "not allow to delete a book which is lent" in new TestLibrary {
    //Given
    val id = library.addNewBook(book1)
    library.lend(id, "some person")

    //When
    library.removeBookById(id)

    //Then
    libraryStore must have size 1
  }

//  Should allow to lent a book by ID ( should be forbidden if copy with given ID is already lent). Should allow to pass the name of the person who lend the book.

  it should "allow to lend a book which is not yet lent" in new TestLibrary {
    //Given
    val id = library.addNewBook(book1)
    val person = "Michael"

    //When
    val result = library.lend(id, person)

    //Then
    result must be(Library.LendOperationSucceeded)
    lendStore must have size 1
    lendStore must contain(id, LendInfo(person))
  }

  it should "not allow to lend a book which is already lent" in new TestLibrary {
    //Given
    val id = library.addNewBook(book1)
    library.lend(id, person)

    //When
    val result = library.lend(id, person)

    //Then
    result must be(Library.LendOperationFailed)
    lendStore must have size 1
  }

  it should "list an empty library" in new TestLibrary {
    //When
    val result = library.list()

    //Then
    result must be (empty)
  }

  it should "list some library with books" in new TestLibrary {
    //Given
    val id1 = library.addNewBook(book1)
    val id2 = library.addNewBook(book1)
    val id3 = library.addNewBook(book2)

    library.lend(id2, person)

    //When
    val result = library.list()

    //Then
    result must have size 2
    result must contain allOf(BookInfo(book1, LendInformation(1, 1)), BookInfo(book2, LendInformation(1, 0)))
  }

  it should "search by title for a non-existing title" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.search(SearchQuery(title = Some(NonExistingTitle)))

    //Then
    result must be (empty)
  }

  it should "search by title for an existing title" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.search(SearchQuery(title = Some(book1.title)))

    //Then
    result must have size 1
    result must contain (BookInfo(book1, LendInformation(1, 0)))
  }

  it should "search by year for a non-existing year" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.search(SearchQuery(year = Some(NonExistingYear)))

    //Then
    result must be (empty)
  }

  it should "search by title for an existing year" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.search(SearchQuery(year = Some(book2.year)))

    //Then
    result must have size 1
    result must contain (BookInfo(book2, LendInformation(1, 0)))
  }

  it should "search by author for a non-existing author" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.search(SearchQuery(author = Some(NonExistingAuthor)))

    //Then
    result must be (empty)
  }

  it should "search by title for an existing author" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.search(SearchQuery(author = Some(book3.author)))

    //Then
    result must have size 2
    result must contain allOf (BookInfo(book3, LendInformation(1, 0)), BookInfo(book4, LendInformation(1, 0)))
  }

  it should "search by title and author for an existing title and non-existing author" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.search(SearchQuery(title = Some(book1.title), author = Some(NonExistingAuthor)))

    //Then
    result must be (empty)
  }

  it should "search by title and author for an existing title and an existing author" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.search(SearchQuery(title = Some(book1.title), author = Some(book1.author)))

    //Then
    result must have size 1
    result must contain (BookInfo(book1, LendInformation(1, 0)))
  }

  it should "not find not existing book in the library" in new TestLibrary {
    //Given
    initializeWithBooks()

    //When
    val result = library.getBookDetailedInfo(-1L)

    //Then
    result must be (empty)
  }

  it should "find the existing book in the library which is not lent" in new TestLibrary {
    //Given
    val list = initializeWithBooks()

    //When
    val result = library.getBookDetailedInfo(list.head)

    //Then
    result must be (Some(BookInfoDetailed(book1, lent = false, person = None)))
  }

  it should "find the existing book in the library which is lent" in new TestLibrary {
    //Given
    val list = initializeWithBooks()
    val id = list.head
    library.lend(id, person)

    //When
    val result = library.getBookDetailedInfo(list.head)

    //Then
    result must be (Some(BookInfoDetailed(book1, lent = true, person = Some(person))))
  }

}

object LibraryTest {
  val book1 = Book("The Fellowship of the Ring", 1954, "J. R. R. Tolkien")
  val book2 = Book("Night Watch", 1998, "Sergei Lukyanenko")
  val book3 = Book("The Last Wish", 1993, "Andrzej Sapkowski")
  val book4 = Book("Sword of Destiny", 1992, "Andrzej Sapkowski")

  val NonExistingTitle = "non-existing title"
  val NonExistingAuthor = "non-existing author"
  val NonExistingYear = 2020

  val person = "Michael"
}

trait TestLibrary {
  val library: LibraryImpl = new LibraryImpl
  def libraryStore: Seq[(Long, Book)] = library.getStore
  def lendStore: Seq[(Long, LendInfo)] = library.getLendStore

  def initializeWithBooks(): List[Long] = {
    val id1 = library.addNewBook(book1)
    val id2 = library.addNewBook(book2)
    val id3 = library.addNewBook(book3)
    val id4 = library.addNewBook(book4)
    List(id1, id2, id3, id4)
  }
}
