import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class sortingVisualizer extends JPanel {
    private int[] array;
    private final int SIZE = 20;
    private final int BAR_WIDTH = 30;
    private final int BAR_HEIGHT_MULTIPLIER = 10;
    private boolean isSorting = false;

    public sortingVisualizer() {
        this.array = generateRandomArray();
        setPreferredSize(new Dimension(SIZE * BAR_WIDTH + 50, 400));
    }

    private int[] generateRandomArray() {
        Random rand = new Random();
        int[] arr = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            arr[i] = rand.nextInt(30) + 1;
        }
        return arr;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < array.length; i++) {
            int height = array[i] * BAR_HEIGHT_MULTIPLIER;
            g.setColor(Color.BLUE);
            g.fillRect(i * BAR_WIDTH + 20, getHeight() - height - 30, BAR_WIDTH - 5, height);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(array[i]), i * BAR_WIDTH + 30, getHeight() - height - 35);
        }
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        repaint();
        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void bubbleSort() {
        new Thread(() -> {
            isSorting = true;
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - i - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        swap(j, j + 1);
                    }
                }
            }
            isSorting = false;
        }).start();
    }

    public void selectionSort() {
        new Thread(() -> {
            isSorting = true;
            for (int i = 0; i < array.length - 1; i++) {
                int minIdx = i;
                for (int j = i + 1; j < array.length; j++) {
                    if (array[j] < array[minIdx]) {
                        minIdx = j;
                    }
                }
                swap(i, minIdx);
            }
            isSorting = false;
        }).start();
    }

    public void insertionSort() {
        new Thread(() -> {
            isSorting = true;
            for (int i = 1; i < array.length; i++) {
                int key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j--;
                    repaint();
                    try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                array[j + 1] = key;
                repaint();
            }
            isSorting = false;
        }).start();
    }

    public void mergeSort() {
        new Thread(() -> {
            isSorting = true;
            mergeSortHelper(0, array.length - 1);
            isSorting = false;
        }).start();
    }

    private void mergeSortHelper(int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortHelper(left, mid);
            mergeSortHelper(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) {
        int[] temp = Arrays.copyOf(array, array.length);
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                array[k++] = temp[i++];
            } else {
                array[k++] = temp[j++];
            }
            repaint();
            try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        while (i <= mid) array[k++] = temp[i++];
        while (j <= right) array[k++] = temp[j++];
        repaint();
    }

    public void heapSort() {
        new Thread(() -> {
            isSorting = true;
            int n = array.length;
            for (int i = n / 2 - 1; i >= 0; i--) heapify(n, i);
            for (int i = n - 1; i > 0; i--) {
                swap(0, i);
                heapify(i, 0);
            }
            isSorting = false;
        }).start();
    }

    private void heapify(int n, int i) {
        int largest = i, left = 2 * i + 1, right = 2 * i + 2;
        if (left < n && array[left] > array[largest]) largest = left;
        if (right < n && array[right] > array[largest]) largest = right;
        if (largest != i) {
            swap(i, largest);
            heapify(n, largest);
        }
    }

    public void resetArray() {
        array = generateRandomArray();
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Visualizer");
        sortingVisualizer panel = new sortingVisualizer();
        JPanel buttonPanel = new JPanel();
        JButton[] buttons = {
                new JButton("Bubble Sort"),
                new JButton("Selection Sort"),
                new JButton("Insertion Sort"),
                new JButton("Merge Sort"),
                new JButton("Heap Sort"),
                new JButton("Reset")
        };

        buttons[0].addActionListener(e -> panel.bubbleSort());
        buttons[1].addActionListener(e -> panel.selectionSort());
        buttons[2].addActionListener(e -> panel.insertionSort());
        buttons[3].addActionListener(e -> panel.mergeSort());
        buttons[4].addActionListener(e -> panel.heapSort());
        buttons[5].addActionListener(e -> panel.resetArray());

        for (JButton button : buttons) buttonPanel.add(button);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
