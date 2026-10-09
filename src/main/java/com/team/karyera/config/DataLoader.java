package com.team.karyera.config;

import com.team.karyera.model.Article;
import com.team.karyera.model.Major;
import com.team.karyera.model.MeetingEvent;
import com.team.karyera.model.Mentor;
import com.team.karyera.repository.ArticleRepository;
import com.team.karyera.repository.MajorRepository;
import com.team.karyera.repository.MeetingEventRepository;
import com.team.karyera.repository.MentorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final MajorRepository majorRepository;
    private final MentorRepository mentorRepository;
    private final ArticleRepository articleRepository;
    private final MeetingEventRepository eventRepository;

    public DataLoader(MajorRepository majorRepository,
                      MentorRepository mentorRepository,
                      ArticleRepository articleRepository,
                      MeetingEventRepository eventRepository) {
        this.majorRepository = majorRepository;
        this.mentorRepository = mentorRepository;
        this.articleRepository = articleRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadMajors();
        loadMentors();
        loadArticles();
        createDemoEvents();
        System.out.println(">>> Yüklənənlər: ixtisas=" + majorRepository.count()
                + ", mentor=" + mentorRepository.count()
                + ", məqalə=" + articleRepository.count()
                + ", görüş=" + eventRepository.count());
    }

    private void loadMajors() throws IOException {
        List<Major> list = new ArrayList<>();
        for (String[] c : readCsv("data/majors.csv", 7)) {
            Major m = new Major();
            m.setId(Long.parseLong(c[0]));
            m.setName(c[1]);
            m.setCategory(c[2]);
            m.setRequiredSubjects(c[3]);
            m.setDescription(c[4]);
            m.setCareers(c[5]);
            m.setUniversities(c[6]);
            list.add(m);
        }
        majorRepository.saveAll(list);
    }

    private void loadMentors() throws IOException {
        List<Mentor> list = new ArrayList<>();
        for (String[] c : readCsv("data/mentors.csv", 5)) {
            Mentor m = new Mentor();
            m.setId(Long.parseLong(c[0]));
            m.setFullName(c[1]);
            m.setProfession(c[2]);
            m.setCategory(c[3]);
            m.setBio(c[4]);
            list.add(m);
        }
        mentorRepository.saveAll(list);
    }

    private void loadArticles() throws IOException {
        List<Article> list = new ArrayList<>();
        for (String[] c : readCsv("data/articles.csv", 4)) {
            Article a = new Article();
            a.setId(Long.parseLong(c[0]));
            a.setCategory(c[1]);
            a.setTitle(c[2]);
            a.setContent(c[3]);
            list.add(a);
        }
        articleRepository.saveAll(list);
    }

    // Tarixlər həmişə "bu gündən sonra" olsun deyə kodda yaradılır
    private void createDemoEvents() {
        List<Mentor> mentors = mentorRepository.findAll();
        int i = 1;
        for (Mentor m : mentors) {
            if (i > 4) {
                break;
            }
            MeetingEvent e = new MeetingEvent();
            e.setTitle(m.getProfession() + " ilə görüş");
            e.setDescription("Şagirdlər üçün açıq görüş: peşə yolu, gündəlik iş və tövsiyələr (nümunə görüş).");
            e.setMentorId(m.getId());
            e.setDateTime(LocalDateTime.now().plusDays(7L * i).withHour(17).withMinute(0).withSecond(0).withNano(0));
            e.setLocation("Onlayn");
            e.setCapacity(30);
            eventRepository.save(e);
            i++;
        }
    }

    public MajorRepository getMajorRepository() {
        return majorRepository;
    }

    public MentorRepository getMentorRepository() {
        return mentorRepository;
    }

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    public MeetingEventRepository getEventRepository() {
        return eventRepository;
    }

    /** CSV-ni oxuyur, başlıq və boş sətirləri atır, hər sətri sütunlara bölür. */
    private List<String[]> readCsv(String path, int expectedColumns) throws IOException {
        List<String[]> rows = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(path);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.readLine(); // başlıq sətri
            String line;
            int lineNo = 1;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) {
                    continue;
                }
                String[] cols = line.split(";", -1);
                if (cols.length != expectedColumns) {
                    throw new IllegalStateException(path + " faylında " + lineNo + "-ci sətirdə "
                            + cols.length + " sütun var, " + expectedColumns + " olmalıdır (mətndə ';' ola bilər)");
                }
                for (int k = 0; k < cols.length; k++) {
                    cols[k] = cols[k].trim();
                }
                rows.add(cols);
            }
        }
        return rows;
    }
}