package studentregistrationsystem;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StudentRegistrationForm extends JFrame {

    // ================= TABLE COLUMNS =================

    private static final int COL_ID = 0;
    private static final int COL_ADMISSION = 1;
    private static final int COL_STUDENT = 2;
    private static final int COL_GENDER = 3;
    private static final int COL_COURSE = 4;
    private static final int COL_EMAIL = 5;

    // ================= COMPONENTS =================

    private HintTextField txtAdmission;
    private HintTextField txtFirstName;
    private HintTextField txtLastName;
    private HintTextField txtEmail;
    private HintTextField txtPhone;
    private HintTextField txtSearch;

    private StyledComboBox cmbGender;
    private StyledComboBox cmbCourse;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JLabel lblTotalStudents;
    private JLabel lblTotalMale;
    private JLabel lblTotalFemale;
    private JLabel lblShowing;

    private RoundedButton btnRegister;
    private RoundedButton btnUpdate;
    private RoundedButton btnDelete;
    private RoundedButton btnClear;

    private StudentRepository repository;
    private int selectedStudentId = -1;
    private int totalStudents = 0;

    // ================= CONSTRUCTOR =================

    public StudentRegistrationForm() {

        repository = new StudentRepository();

        setTitle("StudentHub | University Management");
        setSize(1500, 900);
        setMinimumSize(new Dimension(1150, 750));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Theme.BACKGROUND);

        createInterface();
        loadStudents();
    }

    // ================= MAIN INTERFACE =================

    private void createInterface() {

        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout(0, 22));
        main.setBackground(Theme.BACKGROUND);
        main.setBorder(new EmptyBorder(28, 36, 32, 36));

        main.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);

        content.add(createStatistics(), BorderLayout.NORTH);

        // Registration form 40% / records table 60%
        JPanel workspace = new JPanel(new RatioLayout(0.40, 20));
        workspace.setOpaque(false);

        workspace.add(createRegistrationPanel());
        workspace.add(createTablePanel());

        content.add(workspace, BorderLayout.CENTER);

        main.add(content, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    // ================= SIDEBAR =================

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(248, 0));
        sidebar.setBackground(Theme.WHITE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 0, 1, Theme.BORDER),
                new EmptyBorder(24, 16, 20, 16)
        ));

        // Brand

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        brand.setOpaque(false);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        RoundedPanel logo = new RoundedPanel(14, Theme.PRIMARY, null);
        logo.setLayout(new GridBagLayout());
        logo.setPreferredSize(new Dimension(40, 40));

        JLabel logoText = new JLabel("KU");
        logoText.setFont(Theme.font(Font.BOLD, 15));
        logoText.setForeground(Color.WHITE);
        logo.add(logoText);

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));

        JLabel brandName = new JLabel("StudentHub");
        brandName.setFont(Theme.font(Font.BOLD, 17));
        brandName.setForeground(Theme.TEXT);

        JLabel brandSub = new JLabel("ADMINISTRATION");
        brandSub.setFont(Theme.font(Font.PLAIN, 10));
        brandSub.setForeground(Theme.MUTED);

        brandText.add(brandName);
        brandText.add(brandSub);

        brand.add(logo);
        brand.add(brandText);

        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(34));

        // Menu

        JLabel menuTitle = new JLabel("WORKSPACE");
        menuTitle.setFont(Theme.font(Font.BOLD, 11));
        menuTitle.setForeground(Theme.MUTED);
        menuTitle.setBorder(new EmptyBorder(0, 14, 10, 0));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(menuTitle);

        sidebar.add(createNavigationItem(
                VectorIcon.Type.HOME, "Dashboard", true));
        sidebar.add(Box.createVerticalStrut(4));

        sidebar.add(createNavigationItem(
                VectorIcon.Type.USERS, "Students", false));
        sidebar.add(Box.createVerticalStrut(4));

        sidebar.add(createNavigationItem(
                VectorIcon.Type.PLUS, "Registration", false));

        sidebar.add(Box.createVerticalGlue());

        // Info card

        RoundedPanel info = new RoundedPanel(14, Theme.PRIMARY_SOFT, null);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(16, 16, 16, 16));
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel infoTitle = new JLabel("Student Portal");
        infoTitle.setFont(Theme.font(Font.BOLD, 14));
        infoTitle.setForeground(Theme.PRIMARY_DARK);
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoText = new JLabel(
                "<html><body style='width:150px'>"
                + "Manage student information and academic records "
                + "in one place.</body></html>"
        );
        infoText.setFont(Theme.font(Font.PLAIN, 12));
        infoText.setForeground(Theme.TEXT_LABEL);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(infoTitle);
        info.add(Box.createVerticalStrut(6));
        info.add(infoText);

        sidebar.add(info);
        sidebar.add(Box.createVerticalStrut(16));

        JLabel version = new JLabel("StudentHub v2.0");
        version.setFont(Theme.font(Font.PLAIN, 11));
        version.setForeground(Theme.MUTED);
        version.setBorder(new EmptyBorder(0, 4, 0, 0));
        version.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(version);

        return sidebar;
    }

    private JComponent createNavigationItem(
            VectorIcon.Type iconType,
            String title,
            boolean active) {

        Color foreground = active ? Theme.PRIMARY_DARK : Theme.TEXT_LABEL;
        Color transparent = new Color(0, 0, 0, 0);

        RoundedPanel item = new RoundedPanel(
                12,
                active ? Theme.PRIMARY_SOFT : transparent,
                null
        );

        item.setLayout(new BorderLayout());
        item.setPreferredSize(new Dimension(200, 44));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        item.setBorder(new EmptyBorder(0, 14, 0, 10));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(
                title,
                new VectorIcon(iconType, 20, foreground),
                SwingConstants.LEFT
        );
        label.setIconTextGap(12);
        label.setFont(Theme.font(Font.BOLD, 14));
        label.setForeground(foreground);

        item.add(label, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    item.setBackground(Theme.HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) {
                    item.setBackground(transparent);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                if (title.equals("Registration")) {
                    txtAdmission.requestFocus();
                }

                if (title.equals("Students")) {
                    studentTable.requestFocus();
                }

                if (title.equals("Dashboard")) {
                    txtSearch.setText("");
                    loadStudents();
                    clearFields();
                }
            }
        });

        return item;
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(Theme.font(Font.BOLD, 28));
        title.setForeground(Theme.TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Welcome back! Here is what is happening with your students."
        );
        subtitle.setFont(Theme.font(Font.PLAIN, 14));
        subtitle.setForeground(Theme.MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(title);
        left.add(Box.createVerticalStrut(6));
        left.add(subtitle);

        RoundedPanel pill = new RoundedPanel(40, Theme.WHITE, Theme.BORDER);
        pill.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));

        JLabel admin = new JLabel("Administrator");
        admin.setFont(Theme.font(Font.BOLD, 13));
        admin.setForeground(Theme.PRIMARY_DARK);
        admin.setBorder(new EmptyBorder(0, 0, 0, 10));

        pill.add(new AvatarView("A", Theme.PRIMARY, 32));
        pill.add(admin);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(pill);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ================= STATISTICS =================

    private JPanel createStatistics() {

        JPanel stats = new JPanel(new GridLayout(1, 3, 20, 0));
        stats.setOpaque(false);

        lblTotalStudents = new JLabel("0");
        lblTotalMale = new JLabel("0");
        lblTotalFemale = new JLabel("0");

        stats.add(createStatCard(
                "TOTAL STUDENTS",
                lblTotalStudents,
                "All registered students",
                VectorIcon.Type.USERS,
                Theme.PRIMARY,
                Theme.PRIMARY_SOFT
        ));

        stats.add(createStatCard(
                "MALE STUDENTS",
                lblTotalMale,
                "Male registrations",
                VectorIcon.Type.USER,
                Theme.AMBER,
                Theme.AMBER_SOFT
        ));

        stats.add(createStatCard(
                "FEMALE STUDENTS",
                lblTotalFemale,
                "Female registrations",
                VectorIcon.Type.USER,
                Theme.PINK,
                Theme.PINK_SOFT
        ));

        stats.setPreferredSize(new Dimension(0, 112));

        return stats;
    }

    private JPanel createStatCard(
            String heading,
            JLabel value,
            String description,
            VectorIcon.Type iconType,
            Color accent,
            Color soft) {

        RoundedPanel card = createCard();

        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(18, 24, 18, 24));

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(heading);
        title.setFont(Theme.font(Font.BOLD, 12));
        title.setForeground(Theme.MUTED);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        value.setFont(Theme.font(Font.BOLD, 34));
        value.setForeground(Theme.TEXT);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(description);
        sub.setFont(Theme.font(Font.PLAIN, 12));
        sub.setForeground(Theme.MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        details.add(title);
        details.add(Box.createVerticalStrut(2));
        details.add(value);
        details.add(sub);

        JPanel iconWrap = new JPanel(new GridBagLayout());
        iconWrap.setOpaque(false);
        iconWrap.add(new IconTile(iconType, accent, soft));

        card.add(details, BorderLayout.CENTER);
        card.add(iconWrap, BorderLayout.EAST);

        return card;
    }

    // ================= CARD HELPER =================

    private RoundedPanel createCard() {

        RoundedPanel card = new RoundedPanel(16, Theme.WHITE, Theme.BORDER);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        return card;
    }

    // ================= REGISTRATION PANEL =================

    private JPanel createRegistrationPanel() {

        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(0, 18));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Student Registration");
        title.setFont(Theme.font(Font.BOLD, 19));
        title.setForeground(Theme.TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Add a new student to the system");
        subtitle.setFont(Theme.font(Font.PLAIN, 13));
        subtitle.setForeground(Theme.MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(subtitle);

        // Input fields

        txtAdmission = new HintTextField("e.g. KU/2026/021");
        txtFirstName = new HintTextField("First name");
        txtLastName = new HintTextField("Last name");
        txtEmail = new HintTextField("name@example.com");
        txtPhone = new HintTextField("+254 7XX XXX XXX");

        cmbGender = new StyledComboBox(new String[]{
            "Select Gender",
            "Male",
            "Female",
            "Other"
        });

        cmbCourse = new StyledComboBox(new String[]{
            "Select Course",
            "Software Engineering",
            "Computer Science",
            "Information Technology",
            "Business Information Technology",
            "Computer Technology",
            "Data Science",
            "Cyber Security"
        });

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTH;

        addField(fields, gbc, 0, 0, 2,
                createFieldGroup("Admission Number", txtAdmission));
        addField(fields, gbc, 0, 1, 1,
                createFieldGroup("First Name", txtFirstName));
        addField(fields, gbc, 1, 1, 1,
                createFieldGroup("Last Name", txtLastName));
        addField(fields, gbc, 0, 2, 1,
                createFieldGroup("Gender", cmbGender));
        addField(fields, gbc, 1, 2, 1,
                createFieldGroup("Course", cmbCourse));
        addField(fields, gbc, 0, 3, 2,
                createFieldGroup("Email Address", txtEmail));
        addField(fields, gbc, 0, 4, 2,
                createFieldGroup("Phone Number", txtPhone));

        // Buttons

        btnRegister = new RoundedButton(
                "Register Student", RoundedButton.Kind.PRIMARY);
        btnUpdate = new RoundedButton(
                "Update Student", RoundedButton.Kind.OUTLINE);
        btnDelete = new RoundedButton(
                "Delete Student", RoundedButton.Kind.DANGER);
        btnClear = new RoundedButton(
                "Clear form", RoundedButton.Kind.GHOST);

        btnRegister.setPreferredSize(new Dimension(100, 46));
        btnClear.setPreferredSize(new Dimension(100, 40));

        JPanel actions = new JPanel(new GridBagLayout());
        actions.setOpaque(false);

        GridBagConstraints a = new GridBagConstraints();
        a.fill = GridBagConstraints.HORIZONTAL;
        a.weightx = 1;

        a.gridx = 0;
        a.gridy = 0;
        a.gridwidth = 2;
        a.insets = new Insets(0, 0, 12, 0);
        actions.add(btnRegister, a);

        a.gridy = 1;
        a.gridwidth = 1;
        a.insets = new Insets(0, 0, 12, 6);
        actions.add(btnUpdate, a);

        a.gridx = 1;
        a.insets = new Insets(0, 6, 12, 0);
        actions.add(btnDelete, a);

        a.gridx = 0;
        a.gridy = 2;
        a.gridwidth = 2;
        a.insets = new Insets(0, 0, 0, 0);
        actions.add(btnClear, a);

        JPanel formContent = new JPanel(new BorderLayout(0, 14));
        formContent.setOpaque(false);

        formContent.add(fields, BorderLayout.CENTER);
        formContent.add(actions, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(formContent);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        SlimScrollBarUI.apply(scroll);

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        btnRegister.addActionListener(e -> registerStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearFields());

        return panel;
    }

    // ================= FIELD COMPONENTS =================

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int x, int y, int span,
            JComponent component) {

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = span;

        if (span == 2) {
            gbc.insets = new Insets(6, 0, 6, 0);
        } else if (x == 0) {
            gbc.insets = new Insets(6, 0, 6, 8);
        } else {
            gbc.insets = new Insets(6, 8, 6, 0);
        }

        panel.add(component, gbc);
    }

    private JPanel createFieldGroup(String label, JComponent component) {

        JPanel group = new JPanel();
        group.setOpaque(false);
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(label);
        title.setFont(Theme.font(Font.BOLD, 13));
        title.setForeground(Theme.TEXT_SOFT);
        title.setLabelFor(component);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setPreferredSize(new Dimension(100, 42));
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        group.add(title);
        group.add(Box.createVerticalStrut(6));
        group.add(component);

        return group;
    }

    // ================= STUDENT TABLE =================

    private JPanel createTablePanel() {

        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(0, 16));

        // Title + search row

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Student Records");
        title.setFont(Theme.font(Font.BOLD, 19));
        title.setForeground(Theme.TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "View, search and manage registered students"
        );
        subtitle.setFont(Theme.font(Font.PLAIN, 13));
        subtitle.setForeground(Theme.MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitle);

        txtSearch = new HintTextField(
                "Search by name, admission no. or email",
                new VectorIcon(VectorIcon.Type.SEARCH, 18, Theme.MUTED)
        );
        txtSearch.setToolTipText(
                "Search by admission number, student name or email"
        );

        RoundedButton search = new RoundedButton(
                "Search", RoundedButton.Kind.PRIMARY);
        RoundedButton refresh = new RoundedButton(
                "Refresh", RoundedButton.Kind.OUTLINE);

        search.setPreferredSize(new Dimension(90, 42));
        refresh.setPreferredSize(new Dimension(90, 42));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttons.setOpaque(false);
        buttons.add(search);
        buttons.add(Box.createHorizontalStrut(8));
        buttons.add(refresh);

        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        searchRow.setOpaque(false);
        searchRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        searchRow.add(txtSearch, BorderLayout.CENTER);
        searchRow.add(buttons, BorderLayout.EAST);

        top.add(titlePanel);
        top.add(Box.createVerticalStrut(16));
        top.add(searchRow);

        // Table

        String[] columns = {
            "ID",
            "Admission No.",
            "Student",
            "Gender",
            "Course",
            "Email"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);

        studentTable.setFont(Theme.font(Font.PLAIN, 13));
        studentTable.setForeground(Theme.TEXT);
        studentTable.setBackground(Theme.WHITE);
        studentTable.setRowHeight(52);
        studentTable.setShowVerticalLines(false);
        studentTable.setShowHorizontalLines(true);
        studentTable.setGridColor(Theme.ROW_LINE);
        studentTable.setIntercellSpacing(new Dimension(0, 1));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setSelectionBackground(Theme.ROW_SELECTED);
        studentTable.setSelectionForeground(Theme.TEXT);
        studentTable.setFillsViewportHeight(true);
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader tableHeader = studentTable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setPreferredSize(new Dimension(0, 40));
        tableHeader.setDefaultRenderer(new HeaderRenderer());

        studentTable.getColumnModel().getColumn(COL_ID)
                .setCellRenderer(new PlainRenderer(Theme.MUTED));
        studentTable.getColumnModel().getColumn(COL_ADMISSION)
                .setCellRenderer(new PlainRenderer(Theme.TEXT_SOFT));
        studentTable.getColumnModel().getColumn(COL_STUDENT)
                .setCellRenderer(new StudentRenderer());
        studentTable.getColumnModel().getColumn(COL_GENDER)
                .setCellRenderer(new BadgeRenderer(false));
        studentTable.getColumnModel().getColumn(COL_COURSE)
                .setCellRenderer(new BadgeRenderer(true));
        studentTable.getColumnModel().getColumn(COL_EMAIL)
                .setCellRenderer(new PlainRenderer(Theme.TEXT_LABEL));

        int[] widths = {50, 125, 190, 95, 210, 210};
        int[] minimums = {40, 100, 140, 80, 120, 120};

        for (int i = 0; i < widths.length; i++) {
            studentTable.getColumnModel().getColumn(i)
                    .setPreferredWidth(widths[i]);
            studentTable.getColumnModel().getColumn(i)
                    .setMinWidth(minimums[i]);
        }

        JScrollPane scroll = new JScrollPane(studentTable);
        scroll.setBorder(new LineBorder(Theme.ROW_LINE, 1));
        scroll.getViewport().setBackground(Theme.WHITE);
        SlimScrollBarUI.apply(scroll);

        JPanel corner = new JPanel();
        corner.setBackground(Theme.HEADER_BG);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, corner);

        // Footer

        lblShowing = new JLabel(" ");
        lblShowing.setFont(Theme.font(Font.PLAIN, 13));
        lblShowing.setForeground(Theme.MUTED);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(0, 2, 0, 0));
        footer.add(lblShowing, BorderLayout.WEST);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);

        // Select record for editing

        studentTable.getSelectionModel()
                .addListSelectionListener(e -> {

            int row = studentTable.getSelectedRow();

            if (!e.getValueIsAdjusting() && row != -1) {

                Student student = (Student) tableModel
                        .getValueAt(row, COL_STUDENT);

                selectedStudentId = student.getId();

                txtAdmission.setText(nz(student.getAdmissionNumber()));
                txtFirstName.setText(nz(student.getFirstName()));
                txtLastName.setText(nz(student.getLastName()));
                txtEmail.setText(nz(student.getEmail()));
                txtPhone.setText(nz(student.getPhone()));

                select(cmbGender, student.getGender());
                select(cmbCourse, student.getCourse());
            }
        });

        search.addActionListener(e -> searchStudent());
        txtSearch.addActionListener(e -> searchStudent());

        refresh.addActionListener(e -> {
            txtSearch.setText("");
            loadStudents();
            clearFields();
        });

        return panel;
    }

    // ================= CRUD OPERATIONS =================

    private void registerStudent() {

        if (!validateFields()) {
            return;
        }

        Student student = getStudentFromForm();

        if (repository.addStudent(student)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Student registered successfully!",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadStudents();
            clearFields();
        }
    }

    private void updateStudent() {

        if (selectedStudentId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a student from the table first.",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validateFields()) {
            return;
        }

        Student student = getStudentFromForm();
        student.setId(selectedStudentId);

        if (repository.updateStudent(student)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Student updated successfully!",
                    "Update Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadStudents();
            clearFields();
        }
    }

    private void deleteStudent() {

        if (selectedStudentId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a student to delete.",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this student?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {

            if (repository.deleteStudent(selectedStudentId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Student deleted successfully.",
                        "Deletion Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadStudents();
                clearFields();
            }
        }
    }

    private void searchStudent() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadStudents();
            return;
        }

        List<Student> results = repository.searchStudents(keyword);

        fillTable(results);

        if (results.isEmpty()) {
            lblShowing.setText(
                    "No students match \u201C" + keyword + "\u201D");
        } else {
            lblShowing.setText("Showing " + results.size()
                    + " of " + totalStudents + " students");
        }
    }

    // ================= TABLE & STATISTICS =================

    private void loadStudents() {

        List<Student> students = repository.getAllStudents();

        fillTable(students);
        updateStatistics(students);

        if (students.isEmpty()) {
            lblShowing.setText("No students registered yet");
        } else if (students.size() == 1) {
            lblShowing.setText("Showing 1 student");
        } else {
            lblShowing.setText("Showing all "
                    + students.size() + " students");
        }
    }

    private void fillTable(List<Student> students) {

        tableModel.setRowCount(0);

        for (Student student : students) {

            tableModel.addRow(new Object[]{
                student.getId(),
                student.getAdmissionNumber(),
                student,
                student.getGender(),
                student.getCourse(),
                student.getEmail()
            });
        }
    }

    private void updateStatistics(List<Student> students) {

        int male = 0;
        int female = 0;

        for (Student student : students) {

            if ("Male".equalsIgnoreCase(student.getGender())) {
                male++;
            }

            if ("Female".equalsIgnoreCase(student.getGender())) {
                female++;
            }
        }

        totalStudents = students.size();

        lblTotalStudents.setText(String.valueOf(totalStudents));
        lblTotalMale.setText(String.valueOf(male));
        lblTotalFemale.setText(String.valueOf(female));
    }

    // ================= FORM HELPERS =================

    private Student getStudentFromForm() {

        return new Student(
                0,
                txtAdmission.getText().trim(),
                txtFirstName.getText().trim(),
                txtLastName.getText().trim(),
                cmbGender.getSelectedItem().toString(),
                cmbCourse.getSelectedItem().toString(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim()
        );
    }

    private boolean validateFields() {

        if (txtAdmission.getText().trim().isEmpty()
                || txtFirstName.getText().trim().isEmpty()
                || txtLastName.getText().trim().isEmpty()
                || cmbGender.getSelectedIndex() == 0
                || cmbCourse.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        String email = txtEmail.getText().trim();

        if (!email.isEmpty()
                && !email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE
            );

            txtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void clearFields() {

        txtAdmission.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");

        cmbGender.setSelectedIndex(0);
        cmbCourse.setSelectedIndex(0);

        selectedStudentId = -1;

        studentTable.clearSelection();

        txtAdmission.requestFocus();
    }

    private String nz(String value) {
        return value == null ? "" : value;
    }

    /** Selects the matching item, or the placeholder if there is none. */
    private void select(JComboBox<String> combo, String value) {

        for (int i = 0; i < combo.getItemCount(); i++) {

            if (combo.getItemAt(i).equalsIgnoreCase(nz(value))) {
                combo.setSelectedIndex(i);
                return;
            }
        }

        combo.setSelectedIndex(0);
    }

    // ================= CUSTOM UI PIECES =================

    /** Lays out two children side by side: the first gets leftShare of the width. */
    private static class RatioLayout implements LayoutManager {

        private final double leftShare;
        private final int gap;

        RatioLayout(double leftShare, int gap) {
            this.leftShare = leftShare;
            this.gap = gap;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(1000, 600);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(700, 400);
        }

        @Override
        public void layoutContainer(Container parent) {

            if (parent.getComponentCount() < 2) {
                return;
            }

            Insets in = parent.getInsets();

            int width = parent.getWidth() - in.left - in.right - gap;
            int height = parent.getHeight() - in.top - in.bottom;
            int leftWidth = (int) (width * leftShare);

            parent.getComponent(0).setBounds(
                    in.left, in.top, leftWidth, height);

            parent.getComponent(1).setBounds(
                    in.left + leftWidth + gap, in.top,
                    width - leftWidth, height);
        }
    }

    /** Small circular avatar with initials. */
    private static class AvatarView extends JComponent {

        private final String initials;
        private final Color color;
        private final int size;

        AvatarView(String initials, Color color, int size) {
            this.initials = initials;
            this.color = color;
            this.size = size;
            setPreferredSize(new Dimension(size, size));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Theme.quality(g2);
            Theme.drawAvatar(g2, 0, 0, size, initials, color);
            g2.dispose();
        }
    }

    /** Rounded coloured square holding a line icon (used on the stat cards). */
    private static class IconTile extends JComponent {

        private final VectorIcon icon;
        private final Color background;

        IconTile(VectorIcon.Type type, Color foreground, Color background) {
            this.icon = new VectorIcon(type, 24, foreground);
            this.background = background;
            setPreferredSize(new Dimension(52, 52));
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            Theme.quality(g2);

            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            icon.paintIcon(this, g2,
                    (getWidth() - icon.getIconWidth()) / 2,
                    (getHeight() - icon.getIconHeight()) / 2);

            g2.dispose();
        }
    }

    // ================= TABLE RENDERERS =================

    private static class HeaderRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focus, int row, int column) {

            super.getTableCellRendererComponent(table,
                    value == null ? "" : value.toString().toUpperCase(),
                    false, false, row, column);

            setBackground(Theme.HEADER_BG);
            setForeground(Theme.TEXT_LABEL);
            setFont(Theme.font(Font.BOLD, 11));
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 1, 0, Theme.BORDER),
                    new EmptyBorder(0, 12, 0, 12)
            ));

            return this;
        }
    }

    private static class PlainRenderer extends DefaultTableCellRenderer {

        private final Color textColor;

        PlainRenderer(Color textColor) {
            this.textColor = textColor;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focus, int row, int column) {

            super.getTableCellRendererComponent(table, value,
                    selected, false, row, column);

            setBackground(selected ? Theme.ROW_SELECTED : Theme.WHITE);
            setForeground(textColor);
            setFont(Theme.font(Font.PLAIN, 13));
            setBorder(new EmptyBorder(0, 12, 0, 12));

            return this;
        }
    }

    /** Avatar circle + full name. */
    private static class StudentRenderer extends JPanel
            implements TableCellRenderer {

        private Student student;

        StudentRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focus, int row, int column) {

            student = value instanceof Student ? (Student) value : null;
            setBackground(selected ? Theme.ROW_SELECTED : Theme.WHITE);

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            if (student == null) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            Theme.quality(g2);

            int size = 32;
            int y = (getHeight() - size) / 2;

            Theme.drawAvatar(g2, 12, y, size,
                    student.getInitials(),
                    Theme.genderColor(student.getGender()));

            g2.setFont(Theme.font(Font.BOLD, 13));
            g2.setColor(Theme.TEXT);

            FontMetrics fm = g2.getFontMetrics();

            int textX = 12 + size + 12;
            String name = Theme.fit(student.getFullName(), fm,
                    getWidth() - textX - 8);

            g2.drawString(name, textX,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());

            g2.dispose();
        }
    }

    /** Rounded pill for the gender and course columns. */
    private static class BadgeRenderer extends JPanel
            implements TableCellRenderer {

        private final boolean courseMode;

        private String text = "";
        private Color textColor = Theme.TEXT;
        private Color pillColor = Theme.WHITE;

        BadgeRenderer(boolean courseMode) {
            this.courseMode = courseMode;
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focus, int row, int column) {

            text = value == null ? "" : value.toString();

            if (courseMode) {
                textColor = Theme.PRIMARY_DARK;
                pillColor = Theme.PRIMARY_SOFT;
            } else if ("Female".equalsIgnoreCase(text)) {
                textColor = Theme.PINK;
                pillColor = Theme.PINK_SOFT;
            } else if ("Male".equalsIgnoreCase(text)) {
                textColor = Theme.AMBER;
                pillColor = Theme.AMBER_SOFT;
            } else {
                textColor = Theme.SLATE;
                pillColor = Theme.SLATE_SOFT;
            }

            setBackground(selected ? Theme.ROW_SELECTED : Theme.WHITE);

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            if (text.isEmpty()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            Theme.quality(g2);

            g2.setFont(Theme.font(Font.BOLD, 12));

            FontMetrics fm = g2.getFontMetrics();

            String shown = Theme.fit(text, fm, getWidth() - 24 - 20);

            int pillWidth = fm.stringWidth(shown) + 20;
            int pillHeight = 26;
            int x = 12;
            int y = (getHeight() - pillHeight) / 2;

            g2.setColor(pillColor);
            g2.fillRoundRect(x, y, pillWidth, pillHeight,
                    pillHeight, pillHeight);

            g2.setColor(textColor);
            g2.drawString(shown, x + 10,
                    y + (pillHeight - fm.getHeight()) / 2 + fm.getAscent());

            g2.dispose();
        }
    }
}