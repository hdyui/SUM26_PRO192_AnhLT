package service.implement;

import model.Member;
import service.interfaces.IMemberManagement;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class MemberService implements IMemberManagement {

    private List<Member> memberList;
    private static final String FILE_NAME = "members.txt";

    public MemberService() {
        this.memberList = FileUtils.loadMembers(FILE_NAME);
    }

    @Override
    public boolean addMember(Member member) {
        if (member == null) {
            return false;
        }
        // ID phai duy nhat -> vi ID sinh tu SDT nen thuc chat la kiem tra trung SDT
        if (getMemberById(member.getMemberId()) != null) {
            return false;
        }
        memberList.add(member);
        save();
        return true;
    }

    @Override
    public boolean removeMember(String memberId) {
        Member m = getMemberById(memberId);
        if (m == null) {
            return false;
        }
        if (m.getCurrentBorrowed() > 0) {
            return false;
        }
        memberList.remove(m);
        save();
        return true;
    }

    @Override
    public Member getMemberById(String memberId) {
        for (Member m : memberList) {
            if (m.getMemberId().equalsIgnoreCase(memberId)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberList;
    }

    @Override
    public List<Member> searchMembers(String keyword) {
        List<Member> result = new ArrayList<>();
        if (keyword == null) {
            return result;
        }
        String k = keyword.trim().toLowerCase();
        for (Member m : memberList) {
            if (m.getMemberId().toLowerCase().contains(k)
                    || m.getName().toLowerCase().contains(k)
                    || m.getPhone().toLowerCase().contains(k)) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public boolean phoneExists(String phone) {
        if (phone == null) {
            return false;
        }
        for (Member m : memberList) {
            if (m.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateMemberId(String phone) {
        return "M" + phone;
    }

    @Override
    public void save() {
        FileUtils.saveMembers(memberList, FILE_NAME);
    }
}
