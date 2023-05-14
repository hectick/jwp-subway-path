package subway.domain;

import java.util.Objects;

public class Distance {

    private final Integer distance;

    private Distance(final Integer distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance createEmpty() {
        return new Distance(null);
    }

    public static Distance from(final Integer distance) {
        return new Distance(distance);
    }

    private void validateDistance(final Integer distance) {
        if (distance == null) {
            return;
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("[ERROR] 거리는 양의 정수만 가능합니다.");
        }
    }

    public boolean isEmpty() {
        return this.distance == null;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return Objects.equals(distance, distance1.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
