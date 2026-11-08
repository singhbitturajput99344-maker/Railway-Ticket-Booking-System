import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

class TrainInfo {
    int trainNo;
    String trainName;
    String source;
    String destination;
    int availableSeats;
    int totalSeats;

    public TrainInfo(int trainNo, String trainName, String source, String destination, int seats) {
        this.trainNo = trainNo;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.availableSeats = seats;
        this.totalSeats = seats;
    }
}

// Person is the parent class for shared passenger identity details.
class Person {
    private final String name;
    private final int age;
    private final String gender;

    public Person(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}

// Passenger inherits name, age, and gender from Person.
class Passenger extends Person {
    private final String idType;
    private final String idNumber;
    private final String seatPreference;
    private final String berthPreference;
    private final int seatNumber;

    public Passenger(String name, int age, String gender, String idType, String idNumber,
                     String seatPreference, String berthPreference, int seatNumber) {
        // Initialize the inherited Person fields with the superclass constructor.
        super(name, age, gender);
        this.idType = idType;
        this.idNumber = idNumber;
        this.seatPreference = seatPreference;
        this.berthPreference = berthPreference;
        this.seatNumber = seatNumber;
    }

    public String getIdType() {
        return idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getSeatPreference() {
        return seatPreference;
    }

    public String getBerthPreference() {
        return berthPreference;
    }

    public int getSeatNumber() {
        return seatNumber;
    }
}

class Booking {
    private final long pnrNumber;
    private final TrainInfo train;
    private final Date journeyDate;
    private final ArrayList<Passenger> passengers;
    private final String travelClass;
    private String status;

    public Booking(long pnrNumber, TrainInfo train, Date journeyDate, ArrayList<Passenger> passengers) {
        this(pnrNumber, train, journeyDate, passengers, "Sleeper (SL)");
    }

    public Booking(long pnrNumber, TrainInfo train, Date journeyDate,
                   ArrayList<Passenger> passengers, String travelClass) {
        this.pnrNumber = pnrNumber;
        this.train = train;
        this.journeyDate = new Date(journeyDate.getTime());
        this.passengers = new ArrayList<>(passengers);
        this.travelClass = travelClass;
        this.status = "Confirmed";
    }

    public long getPnrNumber() {
        return pnrNumber;
    }

    public TrainInfo getTrain() {
        return train;
    }

    public Date getJourneyDate() {
        return new Date(journeyDate.getTime());
    }

    public ArrayList<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public String getTravelClass() {
        return travelClass;
    }

    public String getStatus() {
        return status;
    }

    public void cancel() {
        status = "Cancelled";
    }
}

public class RailwayGUI {
    private static final Color NAVY = new Color(24, 48, 77);
    private static final Color BLUE = new Color(38, 105, 173);
    private static final Color LIGHT_BACKGROUND = new Color(245, 248, 252);
    private static final Color BORDER = new Color(220, 227, 236);
    private static final String[] BERTH_OPTIONS = {
            "No Preference", "Lower", "Middle", "Upper", "Side Lower", "Side Upper"
    };
    private static final String[] SEAT_OPTIONS = {
            "No Preference", "Lower", "Middle", "Upper", "Side Lower", "Side Upper"
    };
    private static final String[] TRAVEL_CLASS_OPTIONS = {
            "Sleeper (SL)", "Third AC (3A)", "Second AC (2A)", "First AC (1A)"
    };
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");

    private static final ArrayList<TrainInfo> trains = new ArrayList<>();
    private static final ArrayList<Booking> bookings = new ArrayList<>();

    private final JFrame frame = new JFrame("Indian Railway Reservation");
    private final DefaultTableModel trainTableModel = new DefaultTableModel(
            new Object[]{"Train No.", "Train", "From", "To", "Available Seats"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable trainTable = new JTable(trainTableModel);
    private final JTextArea activityArea = new JTextArea();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            trains.add(new TrainInfo(12301, "Rajdhani Express", "Delhi", "Mumbai", 50));
            trains.add(new TrainInfo(12626, "Kerala Express", "New Delhi", "Trivandrum", 30));
            trains.add(new TrainInfo(12951, "Tejas Express", "Delhi", "Chandigarh", 20));
            new RailwayGUI().showWindow();
        });
    }

    private void showWindow() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(950, 620));
        frame.setSize(1120, 720);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(LIGHT_BACKGROUND);
        frame.add(createHeader(), BorderLayout.NORTH);
        frame.add(createMainContent(), BorderLayout.CENTER);
        frame.add(createActionBar(), BorderLayout.SOUTH);
        refreshTrainTable();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(20, 28, 20, 28));

        JLabel title = new JLabel("Indian Railway Reservation");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        JLabel subtitle = new JLabel("Book your journey and manage passenger reservations");
        subtitle.setForeground(new Color(210, 221, 235));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(subtitle);
        header.add(text, BorderLayout.WEST);
        return header;
    }

    private JComponent createMainContent() {
        JPanel content = new JPanel(new GridLayout(1, 2, 16, 0));
        content.setBackground(LIGHT_BACKGROUND);
        content.setBorder(new EmptyBorder(20, 24, 16, 24));

        trainTable.setRowHeight(32);
        trainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trainTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        trainTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        trainTable.setFillsViewportHeight(true);

        JPanel trainPanel = createCardPanel("Available trains");
        trainPanel.add(new JScrollPane(trainTable), BorderLayout.CENTER);
        JLabel hint = new JLabel("Select a train, then choose Book Ticket.");
        hint.setForeground(new Color(100, 111, 126));
        hint.setBorder(new EmptyBorder(10, 0, 0, 0));
        trainPanel.add(hint, BorderLayout.SOUTH);

        JPanel activityPanel = createCardPanel("Booking details");
        activityArea.setEditable(false);
        activityArea.setLineWrap(false);
        activityArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        activityArea.setBackground(Color.WHITE);
        activityArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        activityArea.setText("Welcome!\n\nSelect a train to start a reservation. "
                + "Your ticket and PNR search details will appear here.");
        activityPanel.add(new JScrollPane(activityArea), BorderLayout.CENTER);

        content.add(trainPanel);
        content.add(activityPanel);
        return content;
    }

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        heading.setForeground(NAVY);
        panel.add(heading, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createActionBar() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JButton viewButton = createButton("View Trains", false);
        JButton bookButton = createButton("Book Ticket", true);
        JButton searchButton = createButton("View PNR", false);
        JButton cancelButton = createButton("Cancel Booking", false);

        viewButton.addActionListener(event -> showTrainList());
        bookButton.addActionListener(event -> openBookingDialog());
        searchButton.addActionListener(event -> searchBooking());
        cancelButton.addActionListener(event -> cancelBooking());
        actions.add(viewButton);
        actions.add(bookButton);
        actions.add(searchButton);
        actions.add(cancelButton);
        return actions;
    }

    private JButton createButton(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (primary) {
            button.setBackground(BLUE);
            button.setForeground(Color.WHITE);
            button.setBorder(new EmptyBorder(10, 18, 10, 18));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(NAVY);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(9, 16, 9, 16)));
        }
        return button;
    }

    private void refreshTrainTable() {
        trainTableModel.setRowCount(0);
        for (TrainInfo train : trains) {
            trainTableModel.addRow(new Object[]{
                    train.trainNo, train.trainName, train.source, train.destination, train.availableSeats
            });
        }
    }

    private void showTrainList() {
        StringBuilder text = new StringBuilder("AVAILABLE TRAINS\n\n");
        for (TrainInfo train : trains) {
            text.append("Train No: ").append(train.trainNo)
                    .append(" | ").append(train.trainName)
                    .append(" | ").append(train.source).append(" -> ").append(train.destination)
                    .append(" | Available seats: ").append(train.availableSeats).append('\n');
        }
        activityArea.setText(text.toString());
    }

    private TrainInfo getSelectedTrain() {
        int selectedRow = trainTable.getSelectedRow();
        if (selectedRow >= 0) {
            int trainNo = (Integer) trainTableModel.getValueAt(
                    trainTable.convertRowIndexToModel(selectedRow), 0);
            for (TrainInfo train : trains) {
                if (train.trainNo == trainNo) {
                    return train;
                }
            }
        }

        String input = JOptionPane.showInputDialog(frame, "Enter the train number to book:");
        if (input == null) {
            return null;
        }
        try {
            int trainNo = Integer.parseInt(input.trim());
            for (TrainInfo train : trains) {
                if (train.trainNo == trainNo) {
                    return train;
                }
            }
            JOptionPane.showMessageDialog(frame, "No train was found with that number.",
                    "Train not found", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame, "Enter a valid whole train number.",
                    "Invalid train number", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void openBookingDialog() {
        TrainInfo train = getSelectedTrain();
        if (train == null) {
            return;
        }
        if (train.availableSeats < 1) {
            JOptionPane.showMessageDialog(frame, "There are no available seats on this train.",
                    "Train full", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new BookingDialog(train).setVisible(true);
    }

    private void searchBooking() {
        String input = JOptionPane.showInputDialog(frame, "Enter the 10-digit PNR number:");
        if (input == null) {
            return;
        }
        try {
            long pnrNumber = Long.parseLong(input.trim());
            Booking booking = findBooking(pnrNumber);
            if (booking == null) {
                activityArea.setText("Booking not found for PNR: " + pnrNumber);
            } else {
                activityArea.setText(formatBooking(booking));
                activityArea.setCaretPosition(0);
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame, "Enter a valid PNR number.",
                    "Invalid PNR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatBooking(Booking booking) {
        TrainInfo train = booking.getTrain();
        ArrayList<Passenger> passengers = booking.getPassengers();
        StringBuilder details = new StringBuilder()
                .append("============================================================\n")
                .append("                 INDIAN RAILWAYS E-TICKET\n")
                .append("============================================================\n")
                .append("PNR Number       : ").append(booking.getPnrNumber()).append('\n')
                .append("Train            : ").append(train.trainNo).append(" - ").append(train.trainName).append('\n')
                .append("Journey          : ").append(train.source).append(" -> ").append(train.destination).append('\n')
                .append("Journey Date     : ").append(DATE_FORMAT.format(booking.getJourneyDate())).append('\n')
                .append("Travel Class     : ").append(booking.getTravelClass()).append('\n')
                .append("Booking Status   : ").append(booking.getStatus()).append('\n')
                .append("Total Passengers : ").append(passengers.size()).append('\n')
                .append("------------------------------------------------------------\n")
                .append("PASSENGER DETAILS\n")
                .append("------------------------------------------------------------\n");
        for (int i = 0; i < passengers.size(); i++) {
            Passenger passenger = passengers.get(i);
            details.append(i + 1).append(". ").append(passenger.getName())
                    .append(" | Age: ").append(passenger.getAge())
                    .append(" | Gender: ").append(passenger.getGender()).append('\n')
                    .append("   ID: ").append(passenger.getIdType()).append(" - ")
                    .append(passenger.getIdNumber()).append('\n')
                    .append("   Seat: S1-").append(passenger.getSeatNumber())
                    .append(" | Seat Preference: ").append(passenger.getSeatPreference())
                    .append(" | Berth Preference: ").append(passenger.getBerthPreference()).append("\n\n");
        }
        details.append("============================================================\n")
                .append("One PNR covers all passengers listed above.");
        return details.toString();
    }

    private void cancelBooking() {
        String input = JOptionPane.showInputDialog(frame, "Enter the PNR number to cancel:");
        if (input == null) {
            return;
        }
        final long pnrNumber;
        try {
            pnrNumber = Long.parseLong(input.trim());
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(frame, "Enter a valid PNR number.",
                    "Invalid PNR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = findBooking(pnrNumber);
        if (booking == null) {
            activityArea.setText("Booking not found for PNR: " + pnrNumber);
            return;
        }
        if ("Cancelled".equals(booking.getStatus())) {
            JOptionPane.showMessageDialog(frame, "This booking has already been cancelled.",
                    "Booking cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(frame,
                "Cancel the entire booking for PNR " + pnrNumber + " ("
                        + booking.getPassengers().size() + " passengers)?",
                "Confirm cancellation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            booking.cancel();
            booking.getTrain().availableSeats += booking.getPassengers().size();
            refreshTrainTable();
            activityArea.setText(formatBooking(booking));
            activityArea.setCaretPosition(0);
        }
    }

    private Booking findBooking(long pnrNumber) {
        for (Booking booking : bookings) {
            if (booking.getPnrNumber() == pnrNumber) {
                return booking;
            }
        }
        return null;
    }

    private long createUniquePnr() {
        long pnrNumber;
        do {
            pnrNumber = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        } while (findBooking(pnrNumber) != null);
        return pnrNumber;
    }

    private static boolean isValidPassengerName(String name) {
        return name != null && name.trim().matches("[\\p{L}][\\p{L} .'-]*");
    }

    private class BookingDialog extends JDialog {
        private final TrainInfo train;
        private final ArrayList<PassengerEntryPanel> passengerEntries = new ArrayList<>();
        private final JPanel entriesPanel = new JPanel();
        private final JLabel countLabel = new JLabel();
        private final JSpinner journeyDateSpinner = new JSpinner(
                new SpinnerDateModel(new Date(), new Date(), null, Calendar.DAY_OF_MONTH));
        private final JComboBox<String> travelClassCombo = new JComboBox<>(TRAVEL_CLASS_OPTIONS);
        private JScrollPane entriesScrollPane;

        BookingDialog(TrainInfo train) {
            super(frame, "Book Train - Passenger Details", true);
            this.train = train;
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(930, 690);
            setMinimumSize(new Dimension(760, 560));
            setLocationRelativeTo(frame);
            buildDialog();
        }

        private void buildDialog() {
            JPanel root = new JPanel(new BorderLayout(0, 12));
            root.setBackground(LIGHT_BACKGROUND);
            root.setBorder(new EmptyBorder(18, 20, 16, 20));

            JPanel heading = new JPanel();
            heading.setOpaque(false);
            heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
            JLabel title = new JLabel("Passenger and journey details");
            title.setFont(new Font("SansSerif", Font.BOLD, 21));
            title.setForeground(NAVY);
            JLabel route = new JLabel(train.trainNo + " - " + train.trainName + "  |  "
                    + train.source + " to " + train.destination + "  |  "
                    + train.availableSeats + " seats available");
            route.setFont(new Font("SansSerif", Font.PLAIN, 13));
            route.setForeground(new Color(91, 104, 121));
            heading.add(title);
            heading.add(Box.createVerticalStrut(5));
            heading.add(route);

            JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
            dateRow.setOpaque(false);
            JLabel dateLabel = new JLabel("Journey date:");
            dateLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            journeyDateSpinner.setEditor(new JSpinner.DateEditor(journeyDateSpinner, "dd MMM yyyy"));
            journeyDateSpinner.setPreferredSize(new Dimension(145, 30));
            JLabel travelClassLabel = new JLabel("Travel class:");
            travelClassLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            travelClassCombo.setPreferredSize(new Dimension(155, 30));
            dateRow.add(dateLabel);
            dateRow.add(journeyDateSpinner);
            dateRow.add(travelClassLabel);
            dateRow.add(travelClassCombo);
            heading.add(dateRow);
            root.add(heading, BorderLayout.NORTH);

            entriesPanel.setBackground(LIGHT_BACKGROUND);
            entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
            entriesScrollPane = new JScrollPane(entriesPanel);
            entriesScrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
            entriesScrollPane.getVerticalScrollBar().setUnitIncrement(16);

            JPanel formArea = new JPanel(new BorderLayout(0, 10));
            formArea.setOpaque(false);
            JPanel listHeading = new JPanel(new BorderLayout());
            listHeading.setOpaque(false);
            JLabel hint = new JLabel("Complete the required details for every passenger.");
            hint.setForeground(new Color(91, 104, 121));
            countLabel.setForeground(new Color(91, 104, 121));
            listHeading.add(hint, BorderLayout.WEST);
            listHeading.add(countLabel, BorderLayout.EAST);
            formArea.add(listHeading, BorderLayout.NORTH);
            formArea.add(entriesScrollPane, BorderLayout.CENTER);

            JButton addButton = createButton("Add Passenger (+)", false);
            addButton.addActionListener(event -> addPassengerEntry());
            JPanel addRow = new JPanel(new BorderLayout());
            addRow.setOpaque(false);
            addRow.add(addButton, BorderLayout.WEST);
            formArea.add(addRow, BorderLayout.SOUTH);
            root.add(formArea, BorderLayout.CENTER);

            JButton cancelButton = createButton("Cancel", false);
            JButton confirmButton = createButton("Review & Confirm", true);
            cancelButton.addActionListener(event -> dispose());
            confirmButton.addActionListener(event -> reviewBooking());
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            footer.setOpaque(false);
            footer.add(cancelButton);
            footer.add(confirmButton);
            root.add(footer, BorderLayout.SOUTH);

            setContentPane(root);
            addPassengerEntry();
        }

        private void addPassengerEntry() {
            if (passengerEntries.size() >= train.totalSeats) {
                JOptionPane.showMessageDialog(this,
                        "A booking cannot contain more passengers than this train's total seats.",
                        "Passenger limit reached", JOptionPane.WARNING_MESSAGE);
                return;
            }
            PassengerEntryPanel entry = new PassengerEntryPanel(passengerEntries.size() + 1);
            entry.removeButton.addActionListener(event -> removePassengerEntry(entry));
            passengerEntries.add(entry);
            entriesPanel.add(entry);
            entriesPanel.add(Box.createVerticalStrut(10));
            updateEntryNumbers();
            entriesPanel.revalidate();
            entriesPanel.repaint();
            SwingUtilities.invokeLater(() ->
                    entriesScrollPane.getVerticalScrollBar().setValue(
                            entriesScrollPane.getVerticalScrollBar().getMaximum()));
        }

        private void removePassengerEntry(PassengerEntryPanel entry) {
            if (passengerEntries.size() <= 1) {
                return;
            }
            passengerEntries.remove(entry);
            entriesPanel.removeAll();
            for (PassengerEntryPanel passengerEntry : passengerEntries) {
                entriesPanel.add(passengerEntry);
                entriesPanel.add(Box.createVerticalStrut(10));
            }
            updateEntryNumbers();
            entriesPanel.revalidate();
            entriesPanel.repaint();
        }

        private void updateEntryNumbers() {
            for (int i = 0; i < passengerEntries.size(); i++) {
                passengerEntries.get(i).setPassengerNumber(i + 1);
                passengerEntries.get(i).setRemoveEnabled(passengerEntries.size() > 1);
            }
            countLabel.setText(passengerEntries.size() + " passenger(s)");
        }

        private void reviewBooking() {
            ArrayList<Passenger> passengers = new ArrayList<>();
            for (int i = 0; i < passengerEntries.size(); i++) {
                Passenger passenger = passengerEntries.get(i).createPassenger(i + 1);
                if (passenger == null) {
                    return;
                }
                passengers.add(passenger);
            }

            if (!hasEnoughSeats(passengers.size())) {
                showInsufficientSeats(passengers.size());
                return;
            }
            if (showBookingReview(passengers) == JOptionPane.YES_OPTION) {
                confirmBooking(passengers);
            }
        }

        private boolean hasEnoughSeats(int passengerCount) {
            return train.availableSeats >= passengerCount
                    && getAvailableSeatNumbers(train).size() >= passengerCount;
        }

        private void showInsufficientSeats(int passengerCount) {
            JOptionPane.showMessageDialog(this,
                    "This booking needs " + passengerCount + " seats, but only "
                            + train.availableSeats + " are available. No seats were reserved.",
                    "Not enough seats", JOptionPane.ERROR_MESSAGE);
            refreshTrainTable();
        }

        private int showBookingReview(ArrayList<Passenger> passengers) {
            String[] columns = {"#", "Name", "Age", "Gender", "ID Type", "ID Number",
                    "Seat Preference", "Berth Preference"};
            Object[][] rows = new Object[passengers.size()][columns.length];
            for (int i = 0; i < passengers.size(); i++) {
                Passenger passenger = passengers.get(i);
                rows[i] = new Object[]{
                        i + 1, passenger.getName(), passenger.getAge(), passenger.getGender(),
                        passenger.getIdType(), passenger.getIdNumber(),
                        passenger.getSeatPreference(), passenger.getBerthPreference()
                };
            }

            JTable reviewTable = new JTable(rows, columns);
            reviewTable.setRowHeight(26);
            reviewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            reviewTable.setEnabled(false);
            JScrollPane table = new JScrollPane(reviewTable);
            table.setPreferredSize(new Dimension(820, Math.min(280, 55 + passengers.size() * 26)));

            JPanel reviewPanel = new JPanel(new BorderLayout(0, 10));
            JPanel bookingSummary = new JPanel();
            bookingSummary.setLayout(new BoxLayout(bookingSummary, BoxLayout.Y_AXIS));
            bookingSummary.add(new JLabel("Train: " + train.trainNo + " - " + train.trainName
                    + " (" + train.source + " to " + train.destination + ")"
                    + " | Journey date: " + DATE_FORMAT.format((Date) journeyDateSpinner.getValue())));
            bookingSummary.add(new JLabel("Travel class: " + travelClassCombo.getSelectedItem()));
            reviewPanel.add(bookingSummary, BorderLayout.NORTH);
            reviewPanel.add(table, BorderLayout.CENTER);
            reviewPanel.add(new JLabel("Confirm one booking and one common PNR for "
                    + passengers.size() + " passenger(s)?"), BorderLayout.SOUTH);
            return JOptionPane.showConfirmDialog(this, reviewPanel,
                    "Review booking", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        }

        private void confirmBooking(ArrayList<Passenger> passengers) {
            if (!hasEnoughSeats(passengers.size())) {
                showInsufficientSeats(passengers.size());
                return;
            }

            ArrayList<Integer> seatNumbers = getAvailableSeatNumbers(train);
            if (seatNumbers.size() < passengers.size()) {
                showInsufficientSeats(passengers.size());
                return;
            }

            ArrayList<Passenger> confirmedPassengers = new ArrayList<>();
            for (int i = 0; i < passengers.size(); i++) {
                Passenger passenger = passengers.get(i);
                confirmedPassengers.add(new Passenger(
                        passenger.getName(), passenger.getAge(), passenger.getGender(),
                        passenger.getIdType(), passenger.getIdNumber(),
                        passenger.getSeatPreference(), passenger.getBerthPreference(),
                        seatNumbers.get(i)));
            }

            Booking booking = new Booking(createUniquePnr(), train,
                    (Date) journeyDateSpinner.getValue(), confirmedPassengers,
                    (String) travelClassCombo.getSelectedItem());
            bookings.add(booking);
            train.availableSeats -= confirmedPassengers.size();
            refreshTrainTable();
            activityArea.setText("\uD83C\uDF89 Happy Journey!\n\n" + formatBooking(booking));
            activityArea.setCaretPosition(0);
            dispose();
            JOptionPane.showMessageDialog(frame, createConfirmationPanel(booking),
                    "Booking confirmed", JOptionPane.INFORMATION_MESSAGE);
        }

        private JScrollPane createConfirmationPanel(Booking booking) {
            TrainInfo bookedTrain = booking.getTrain();
            ArrayList<Passenger> bookedPassengers = booking.getPassengers();

            JPanel confirmation = new JPanel(new BorderLayout(0, 14));
            confirmation.setBackground(Color.WHITE);
            confirmation.setBorder(new EmptyBorder(18, 18, 18, 18));
            confirmation.setPreferredSize(new Dimension(790,
                    Math.min(650, 320 + bookedPassengers.size() * 32)));

            JPanel banner = new JPanel();
            banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
            banner.setBackground(new Color(232, 247, 239));
            banner.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(186, 226, 200)),
                    new EmptyBorder(15, 18, 15, 18)));
            JLabel journeyMessage = new JLabel("\uD83C\uDF89 Happy Journey!");
            journeyMessage.setFont(new Font("SansSerif", Font.BOLD, 24));
            journeyMessage.setForeground(new Color(27, 112, 67));
            JLabel confirmedMessage = new JLabel("Your reservation is confirmed. Have a pleasant trip!");
            confirmedMessage.setFont(new Font("SansSerif", Font.PLAIN, 13));
            confirmedMessage.setForeground(new Color(62, 91, 73));
            banner.add(journeyMessage);
            banner.add(Box.createVerticalStrut(5));
            banner.add(confirmedMessage);
            confirmation.add(banner, BorderLayout.NORTH);

            JPanel ticket = new JPanel(new BorderLayout(0, 12));
            ticket.setBackground(Color.WHITE);
            ticket.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(14, 14, 14, 14)));

            JPanel details = new JPanel(new GridLayout(0, 2, 12, 8));
            details.setBackground(Color.WHITE);
            addTicketDetail(details, "PNR NUMBER", String.valueOf(booking.getPnrNumber()));
            addTicketDetail(details, "BOOKING STATUS", booking.getStatus());
            addTicketDetail(details, "TRAVEL CLASS", booking.getTravelClass());
            addTicketDetail(details, "TRAIN", bookedTrain.trainNo + " - " + bookedTrain.trainName);
            addTicketDetail(details, "JOURNEY DATE", DATE_FORMAT.format(booking.getJourneyDate()));
            addTicketDetail(details, "FROM", bookedTrain.source);
            addTicketDetail(details, "TO", bookedTrain.destination);
            ticket.add(details, BorderLayout.NORTH);

            String[] columns = {"#", "Passenger Name", "Age", "Gender", "ID Type / Number",
                    "Seat", "Seat Preference", "Berth Preference"};
            Object[][] rows = new Object[bookedPassengers.size()][columns.length];
            StringBuilder passengerNames = new StringBuilder("Passengers: ");
            for (int i = 0; i < bookedPassengers.size(); i++) {
                Passenger passenger = bookedPassengers.get(i);
                rows[i] = new Object[]{
                        i + 1, passenger.getName(), passenger.getAge(), passenger.getGender(),
                        passenger.getIdType() + " / " + passenger.getIdNumber(),
                        "S1-" + passenger.getSeatNumber(), passenger.getSeatPreference(),
                        passenger.getBerthPreference()
                };
                if (i > 0) {
                    passengerNames.append(", ");
                }
                passengerNames.append(passenger.getName());
            }

            JTable passengerTable = new JTable(rows, columns);
            passengerTable.setEnabled(false);
            passengerTable.setRowHeight(28);
            passengerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            passengerTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
            JScrollPane passengerScrollPane = new JScrollPane(passengerTable);
            passengerScrollPane.setPreferredSize(new Dimension(740,
                    Math.min(240, 32 + bookedPassengers.size() * 28)));

            JPanel passengerSection = new JPanel(new BorderLayout(0, 8));
            passengerSection.setBackground(Color.WHITE);
            JLabel passengerHeading = new JLabel("PASSENGER DETAILS  (" + bookedPassengers.size() + ")");
            passengerHeading.setFont(new Font("SansSerif", Font.BOLD, 14));
            passengerHeading.setForeground(NAVY);
            passengerSection.add(passengerHeading, BorderLayout.NORTH);
            passengerSection.add(passengerScrollPane, BorderLayout.CENTER);

            JPanel content = new JPanel(new BorderLayout(0, 12));
            content.setBackground(Color.WHITE);
            JLabel passengerNamesLabel = new JLabel(passengerNames.toString());
            passengerNamesLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            passengerNamesLabel.setForeground(NAVY);
            content.add(passengerNamesLabel, BorderLayout.NORTH);
            content.add(passengerSection, BorderLayout.CENTER);
            ticket.add(content, BorderLayout.CENTER);
            confirmation.add(ticket, BorderLayout.CENTER);

            JLabel footer = new JLabel("One PNR covers all passengers on this ticket.");
            footer.setFont(new Font("SansSerif", Font.ITALIC, 12));
            footer.setForeground(new Color(91, 104, 121));
            confirmation.add(footer, BorderLayout.SOUTH);

            JScrollPane scrollPane = new JScrollPane(confirmation);
            scrollPane.setPreferredSize(new Dimension(830, 590));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            return scrollPane;
        }

        private void addTicketDetail(JPanel panel, String label, String value) {
            JPanel field = new JPanel();
            field.setLayout(new BoxLayout(field, BoxLayout.Y_AXIS));
            field.setBackground(Color.WHITE);
            JLabel fieldLabel = new JLabel(label);
            fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
            fieldLabel.setForeground(new Color(100, 111, 126));
            JLabel fieldValue = new JLabel(value);
            fieldValue.setFont(new Font("SansSerif", Font.BOLD, 13));
            fieldValue.setForeground(NAVY);
            field.add(fieldLabel);
            field.add(Box.createVerticalStrut(3));
            field.add(fieldValue);
            panel.add(field);
        }
    }

    private class PassengerEntryPanel extends JPanel {
        private final JLabel heading = new JLabel();
        private final JTextField nameField = new JTextField();
        private final JTextField ageField = new JTextField();
        private final JComboBox<String> genderField = new JComboBox<>(
                new String[]{"Select gender", "Male", "Female", "Other"});
        private final JComboBox<String> idTypeField = new JComboBox<>(
                new String[]{"Select ID type", "Aadhaar", "Passport", "Driving Licence", "Voter ID"});
        private final JTextField idNumberField = new JTextField();
        private final JComboBox<String> seatPreferenceField = new JComboBox<>(SEAT_OPTIONS);
        private final JComboBox<String> berthPreferenceField = new JComboBox<>(BERTH_OPTIONS);
        private final JButton removeButton = createButton("Remove Passenger (-)", false);

        PassengerEntryPanel(int passengerNumber) {
            setLayout(new GridBagLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(10, 10, 10, 10)));

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(3, 5, 3, 5);
            constraints.fill = GridBagConstraints.HORIZONTAL;

            heading.setFont(new Font("SansSerif", Font.BOLD, 13));
            heading.setForeground(NAVY);
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 3;
            constraints.weightx = 1;
            add(heading, constraints);
            constraints.gridx = 3;
            constraints.gridwidth = 1;
            constraints.weightx = 0;
            constraints.anchor = GridBagConstraints.EAST;
            add(removeButton, constraints);

            addField("Passenger Name", nameField, 0, 1);
            addField("Age", ageField, 1, 1);
            addField("Gender", genderField, 2, 1);
            addField("Passenger ID Type", idTypeField, 0, 3);
            addField("Passenger ID Number", idNumberField, 1, 3);
            addField("Seat Preference", seatPreferenceField, 2, 3);
            addField("Berth Preference", berthPreferenceField, 0, 5);
            setPassengerNumber(passengerNumber);
        }

        private void addField(String label, JComponent field, int column, int row) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = column;
            constraints.gridy = row;
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(3, 5, 3, 5);
            JLabel fieldLabel = new JLabel(label);
            fieldLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            add(fieldLabel, constraints);
            constraints.gridy = row + 1;
            field.setPreferredSize(new Dimension(155, 29));
            add(field, constraints);
        }

        private void setPassengerNumber(int number) {
            heading.setText("Passenger " + number);
        }

        private void setRemoveEnabled(boolean enabled) {
            removeButton.setEnabled(enabled);
        }

        private Passenger createPassenger(int passengerNumber) {
            String name = nameField.getText().trim();
            if (!isValidPassengerName(name)) {
                showFieldError("Passenger " + passengerNumber
                        + ": enter a valid name using letters, spaces, apostrophes, or hyphens.", nameField);
                return null;
            }

            final int age;
            try {
                age = Integer.parseInt(ageField.getText().trim());
            } catch (NumberFormatException exception) {
                showFieldError("Passenger " + passengerNumber + ": age must be a whole number.", ageField);
                return null;
            }
            if (age < 1 || age > 120) {
                showFieldError("Passenger " + passengerNumber + ": age must be between 1 and 120.", ageField);
                return null;
            }

            String gender = (String) genderField.getSelectedItem();
            if (gender == null || gender.startsWith("Select")) {
                showFieldError("Passenger " + passengerNumber + ": select a gender.", genderField);
                return null;
            }
            String idType = (String) idTypeField.getSelectedItem();
            if (idType == null || idType.startsWith("Select")) {
                showFieldError("Passenger " + passengerNumber + ": select an ID type.", idTypeField);
                return null;
            }

            String idNumber = idNumberField.getText().trim().toUpperCase();
            if (!isValidIdNumber(idType, idNumber)) {
                showFieldError(getIdErrorMessage(passengerNumber, idType), idNumberField);
                return null;
            }

            String seatPreference = (String) seatPreferenceField.getSelectedItem();
            String berthPreference = (String) berthPreferenceField.getSelectedItem();
            return new Passenger(name, age, gender, idType, idNumber,
                    seatPreference, berthPreference, 0);
        }

        private String getIdErrorMessage(int passengerNumber, String idType) {
            String requirement;
            if ("Aadhaar".equals(idType)) {
                requirement = "Aadhaar number must contain exactly 12 digits.";
            } else if ("Passport".equals(idType)) {
                requirement = "Passport number must contain 6 to 12 letters or digits.";
            } else if ("Driving Licence".equals(idType)) {
                requirement = "Driving Licence number must contain 5 to 20 letters, digits, or hyphens.";
            } else {
                requirement = "Voter ID must contain 6 to 12 letters or digits.";
            }
            return "Passenger " + passengerNumber + ": " + requirement;
        }

        private boolean isValidIdNumber(String idType, String idNumber) {
            if ("Aadhaar".equals(idType)) {
                return idNumber.replaceAll("[\\s-]", "").matches("[0-9]{12}");
            }
            if ("Passport".equals(idType)) {
                return idNumber.matches("[A-Z0-9]{6,12}");
            }
            if ("Driving Licence".equals(idType)) {
                return idNumber.matches("[A-Z0-9-]{5,20}");
            }
            if ("Voter ID".equals(idType)) {
                return idNumber.matches("[A-Z0-9]{6,12}");
            }
            return false;
        }

        private void showFieldError(String message, JComponent field) {
            JOptionPane.showMessageDialog(this, message, "Check passenger details", JOptionPane.ERROR_MESSAGE);
            field.requestFocusInWindow();
            if (field instanceof JTextField) {
                ((JTextField) field).selectAll();
            }
        }
    }

    private static ArrayList<Integer> getAvailableSeatNumbers(TrainInfo train) {
        ArrayList<Integer> availableSeats = new ArrayList<>();
        for (int seatNumber = 1; seatNumber <= train.totalSeats; seatNumber++) {
            boolean occupied = false;
            for (Booking booking : bookings) {
                if (booking.getTrain().trainNo == train.trainNo
                        && "Confirmed".equals(booking.getStatus())) {
                    for (Passenger passenger : booking.getPassengers()) {
                        if (passenger.getSeatNumber() == seatNumber) {
                            occupied = true;
                            break;
                        }
                    }
                }
                if (occupied) {
                    break;
                }
            }
            if (!occupied) {
                availableSeats.add(seatNumber);
            }
        }
        return availableSeats;
    }
}
