package org.newnop.repository;

import org.newnop.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
    List<Books> findByAvailableTrue();
    List<Books> findByTitleContainingIgnoreCase(String title);
    List<Books> findByAuthorContainingIgnoreCase(String author);
    List<Books> findByGenreContainingIgnoreCase(String genre);
}