package furhatos.app.cardgame.game

import furhatos.records.Location

/**
 * Handles the location and geometry of the game table
 */
object GameTable {

    val inchInMeters = 0.0254

    /**
     * The diagonal size of the screen (in meters)
     */
    var size = 21 * inchInMeters

    /**
     * The center location of the table (relative to Furhat)
     */
    var centerLocation = Location(0.0, -0.15, 0.5)

    /**
     * The aspect ratio of the screen
     */
    var aspectRatio = 9.0/16.0

    /**
     * The width of the screen (in meters)
     */
    val width
        get() = Math.sqrt(Math.pow(size,2.0)/(Math.pow(aspectRatio,2.0)+1))

    /**
     * The height of the screen (in meters)
     */
    val height
        get() = width * aspectRatio

    /**
     * The top left corner of the table (relative to Furhat)
     */
    val topLeftLocation: Location
        get() = centerLocation.add(Location(-width/2, 0.0, -height/2))

    /**
     * The location where the lowest card should be placed (relative to Furhat)
     */
    val lowestLocation: Location
        get() = centerLocation.add(Location(-width/2.5, 0.0, 0.0))

    /**
     * The location where the highest card should be placed (relative to Furhat)
     */
    val highestLocation: Location
        get() = centerLocation.add(Location(width/2.5, 0.0, 0.0))

}