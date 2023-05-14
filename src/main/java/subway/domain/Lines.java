package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    private Lines(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public static Lines from(final List<Line> lines) {
        return new Lines(lines);
    }

    public void add(final Line line) {
        validateDuplicatedName(line);
        validateDuplicatedColor(line);
        lines.add(line);
    }

    private void validateDuplicatedName(final Line line) {
        boolean result = lines.stream()
                .anyMatch(each -> each.isSameName(line));
        if (result) {
            throw new IllegalArgumentException("[ERROR] 중복되는 이름으로 노선을 생성할 수 없습니다.");
        }
    }

    private void validateDuplicatedColor(final Line line) {
        boolean result = lines.stream()
                .anyMatch(each -> each.isSameColor(line));
        if (result) {
            throw new IllegalArgumentException("[ERROR] 중복되는 색상으로 노선을 생성할 수 없습니다.");
        }
    }

    public Line findById(final long id) {
        return lines.stream()
                .filter(line -> line.isSameId(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 노선을 찾을 수 없습니다."));
    }

    public void remove(final Line oldLine) {
        lines.remove(oldLine);
    }
}
