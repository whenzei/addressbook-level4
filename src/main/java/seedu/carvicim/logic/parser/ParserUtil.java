package seedu.carvicim.logic.parser;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.commons.util.StringUtil;
import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * {@code ParserUtil} contains methods that take in {@code Optional} as parameters. However, it goes against Java's
 * convention (see https://stackoverflow.com/a/39005452) as {@code Optional} should only be used a return type.
 * Justification: The methods in concern receive {@code Optional} return values from other methods as parameters and
 * return {@code Optional} values based on whether the parameters were present. Therefore, it is redundant to unwrap the
 * initial {@code Optional} before passing to {@code ParserUtil} as a parameter and then re-wrap it into an
 * {@code Optional} return value inside {@code ParserUtil} methods.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INSUFFICIENT_PARTS = "Number of parts must be more than 1.";
    public static final String MESSAGE_INSUFFICIENT_WORDS = "Command word to be changed and new command word must "
            + "be provided, separated by a space.";
    public static final String MESSAGE_INVALID_FILENAME = "File name must be the path to an existing file, in the "
            + "same folder as this application";
    public static final String WHITESPACE = "\\s+";
    public static final String APPLICATION_DIRECTORY = ".\\";

    /**
     * Parses {@code multipleWordString} into an {@code String[]} containing command words and returns it.
     * Leading and trailing whitespaces will be trimmed.
     * @throws IllegalValueException if words found is not equal to 2
     */
    public static String[] parseWords(String multipleWordString) throws IllegalValueException {
        String[] commandWords = multipleWordString.trim().split(WHITESPACE);
        if (commandWords.length != 2) {
            throw new IllegalValueException(MESSAGE_INSUFFICIENT_WORDS);
        }
        return commandWords;

    }

    /**
     * Parses {@code filePath} and checks if (@code file) specified exists.
     * Leading and trailing whitespaces will be trimmed.
     * @throws IllegalValueException if file does not exist
     */
    public static String parseFilename(String filePath) throws IllegalValueException {
        File file = new File(filePath.trim());
        if (!file.exists()) {
            throw new IllegalValueException(MESSAGE_INVALID_FILENAME);
        }
        return APPLICATION_DIRECTORY + filePath.trim();

    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code Collection<String> indices} into a {@code Set<Index>}.
     */
    public static ArrayList<Index> parseIndices(Collection<String> indices) throws IllegalValueException {
        requireNonNull(indices);
        final ArrayList<Index> indexList = new ArrayList<>();
        for (String index : indices) {
            indexList.add(parseIndex(index));
        }
        return indexList;
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(parseName(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws IllegalValueException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional<Phone>} if {@code phone} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone) throws IllegalValueException {
        requireNonNull(phone);
        return phone.isPresent() ? Optional.of(parsePhone(phone.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws IllegalValueException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code Optional<String> email} into an {@code Optional<Email>} if {@code email} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Email> parseEmail(Optional<String> email) throws IllegalValueException {
        requireNonNull(email);
        return email.isPresent() ? Optional.of(parseEmail(email.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws IllegalValueException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String vehicleNumber} into a {@code VehicleNumber}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code vehicleNumber} is invalid.
     */
    public static VehicleNumber parseVehicleNumber(String vehicleNumber) throws IllegalValueException {
        requireNonNull(vehicleNumber);
        String trimmedVehicleNumber = vehicleNumber.trim();
        if (!VehicleNumber.isValidVehicleNumber(trimmedVehicleNumber)) {
            throw new IllegalValueException(VehicleNumber.MESSAGE_VEHICLE_ID_CONSTRAINTS);
        }
        return new VehicleNumber(vehicleNumber);
    }

    /**
     * Parses a {@code Optional<String> vehicleNumber} into an {@code Optional<VehicleNumber>}
     * if {@code vehicleNumber} is present.
     *
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<VehicleNumber> parseVehicleNumber(
            Optional<String> vehicleNumber) throws IllegalValueException {

        requireNonNull(vehicleNumber);
        return vehicleNumber.isPresent() ? Optional.of(parseVehicleNumber(vehicleNumber.get())) : Optional.empty();
    }

    //@@author richardson0694
    /**
     * Parses a {@code String date} into a {@code Date}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code date} is invalid.
     */
    public static Date parseDate(String date) throws IllegalValueException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        if (!Date.isValidDate(trimmedDate)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        return new Date(date);
    }

    /**
     * Parses a {@code Optional<String> date} into an {@code Optional<Date>}
     * if {@code date} is present.
     *
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Date> parseDate(Optional<String> date) throws IllegalValueException {
        requireNonNull(date);
        return date.isPresent() ? Optional.of(parseDate(date.get())) : Optional.empty();
    }

}
