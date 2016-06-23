package au.com.iglooit.searchcloud.web.rest;

import au.com.iglooit.searchcloud.domain.Company;
import au.com.iglooit.searchcloud.domain.Member;
import au.com.iglooit.searchcloud.service.MemberService;
import au.com.iglooit.searchcloud.service.MemberSystemService;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@RestController
@RequestMapping("/searchcloud/api")
public class MemberSystemResource {
    private final Logger log = LoggerFactory.getLogger(MemberSystemResource.class);
    @Inject
    private MemberService memberService;
    @Inject
    private MemberSystemService memberSystemService;

    /**
     * SEARCH  /_search/members/:query -> search for the member corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/members/{query:.+}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Member> searchMembers(@PathVariable String query) {
        log.debug("Request to search Members for query {}", query);
        return memberService.search(query);
    }

    @RequestMapping(value = "/_search/company/{companyID}/{query:.+}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Member> searchMembersInCompany(@PathVariable String companyID, @PathVariable String query) {
        log.debug("Request to search Members for query {}", query);
        return memberSystemService.search(companyID, query);
    }

    @RequestMapping(value = "/_upload", headers = "content-type=multipart/*", method = RequestMethod.POST)
    @Timed
    public
    @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("memberID") String memberID,
                            @RequestParam("mobile") String mobile, @RequestParam("name") String name) {
        if (!file.isEmpty()) {
            try {
                String content = DatatypeConverter.printBase64Binary(file.getBytes());
                Member member = new Member();
                member.setCompany(new Company() {{
                    setCompanyName("company five");
                    setId(5L);
                }});
                member.setFile(content);
                member.setMobile(mobile);
                member.setId(Long.valueOf(memberID));
                member.setName(name);
                memberSystemService.saveMemberIntoCloud(member);
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
}
