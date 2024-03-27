package at.technikum.tourplanner.mapper;

public interface Mapper<S, T> {
    T mapToDto(S source);
}
