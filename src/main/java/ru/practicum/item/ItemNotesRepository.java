package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemNotesRepository extends JpaRepository<ItemNote, Long> {
    List<ItemNote> findAllByItemUserIdAndItemUrlContainingIgnoreCase (Long userId, String url);

    @Query("""
            select notes
            from ItemNote as notes
            join notes.item as i
            where i.userId = ?1 and ?2 member of i.tags
            """)
    List<ItemNote> findItemNotesByUserIdAndTag (Long userId, String tag);
}
