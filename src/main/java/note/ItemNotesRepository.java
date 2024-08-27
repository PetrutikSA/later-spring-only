package note;

import note.ItemNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<ItemNote> findAllByItemUserId(Long userId, Pageable page);
}
