package itmo.sleeter.infosys.validator

import itmo.sleeter.infosys.dto.request.yaml.Coordinate
import itmo.sleeter.infosys.dto.request.yaml.Flat
import itmo.sleeter.infosys.dto.request.yaml.House
import itmo.sleeter.infosys.dto.request.yaml.YamlData
import org.springframework.stereotype.Component

data class ValidationError(
    val field: String,
    val message: String
)

@Component
class Validator {

    fun validate(yamlData: YamlData): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        errors.addAll(validateHouseList(yamlData.house))

        errors.addAll(validateFlatList(yamlData.flat))

        return errors
    }

    private fun validateHouseList(houses: List<House>): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()
//        val houseNames = mutableSetOf<String>()

        houses.forEachIndexed { index, house ->
            errors.addAll(validateHouse(house, "house[$index]"))
//            if (!houseNames.add(house.name)) {
//                errors.add(ValidationError("house[$index].name", "House name '${house.name}' must be unique"))
//            }
        }

        return errors
    }

    private fun validateFlatList(flats: List<Flat>): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()
//        val flatNames = mutableSetOf<String>()

        flats.forEachIndexed { index, flat ->
            errors.addAll(validateFlat(flat, "flat[$index]"))

            // Проверка на уникальность имени квартиры
//            if (!flatNames.add(flat.name)) {
//                errors.add(ValidationError("flat[$index].name", "Flat name '${flat.name}' must be unique"))
//            }
        }

        return errors
    }

    private fun validateHouse(house: House, prefix: String): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        if (house.year !in 1..901) {
            errors.add(ValidationError("$prefix.year", "Year must be between 1 and 901"))
        }

        if (house.numberOfLifts < 1) {
            errors.add(ValidationError("$prefix.numberOfLifts", "Number of lifts must be at least 1"))
        }

        return errors
    }

    private fun validateFlat(flat: Flat, prefix: String): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        if (flat.name.isEmpty()) {
            errors.add(ValidationError("$prefix.name", "Name must not be empty"))
        }

        if (flat.area !in 1.0..724.0) {
            errors.add(ValidationError("$prefix.area", "Area must be between 1 and 724"))
        }

        if (flat.price < 1.0) {
            errors.add(ValidationError("$prefix.price", "Price must be at least 1"))
        }

        if (flat.timeToMetroOnFoot < 1) {
            errors.add(ValidationError("$prefix.timeToMetroOnFoot", "Time to metro on foot must be at least 1"))
        }

        if (flat.numberOfRooms < 1) {
            errors.add(ValidationError("$prefix.numberOfRooms", "Number of rooms must be at least 1"))
        }

        if (flat.coordinates != null && flat.coordinates.size > 1) {
            errors.add(ValidationError("flat.coordinates", "Flat assigned more than one coordinates"))
        }
        // Валидация координат, если указаны
        flat.coordinates?.forEachIndexed { index, coordinate ->
            errors.addAll(validateCoordinate(coordinate, "$prefix.coordinates[$index]"))
        }

        if (flat.house != null && flat.house.size > 1) {
            errors.add(ValidationError("flat.house", "Flat assigned more than one house"))
        }
        // Валидация списка House в Flat, если указаны
        flat.house?.forEachIndexed { index, house ->
            errors.addAll(validateHouse(house, "$prefix.house[$index]"))
        }

        return errors
    }

    private fun validateCoordinate(coordinate: Coordinate, prefix: String): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        if (coordinate.x > 598) {
            errors.add(ValidationError("$prefix.x", "X must be less than or equal to 598"))
        }

        if (coordinate.y !in -254.0..Double.MAX_VALUE) {
            errors.add(ValidationError("$prefix.y", "Y must be greater than or equal to -254"))
        }

        return errors
    }
}
