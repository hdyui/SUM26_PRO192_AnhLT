package service.interfaces;

import model.Member;
import java.util.List;

public interface IMemberManagement {

    boolean addMember(Member member);

    boolean removeMember(String memberId);

    Member getMemberById(String memberId);

    List<Member> getAllMembers();

    List<Member> searchMembers(String keyword);

    // Kiem tra SDT da duoc dung cho thanh vien nao khac chua (BR bat buoc truoc khi tao moi)
    boolean phoneExists(String phone);

    // Sinh memberId tu SDT theo dinh dang M[SDT]
    String generateMemberId(String phone);

    void save();
}
