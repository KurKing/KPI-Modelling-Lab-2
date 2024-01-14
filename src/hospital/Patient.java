package hospital;

public enum Patient {

    FIRST_TYPE, SECOND_TYPE, THIRD_TYPE;

    public double getCreationDelay() {

        switch (this) {
            case FIRST_TYPE:
                return 0.5;
            case SECOND_TYPE:
                return 0.1;
            case THIRD_TYPE:
                return 0.4;
        }

        return 0.0;
    }

    public String getName() {

        switch (this) {
            case FIRST_TYPE:
                return "Type 1";
            case SECOND_TYPE:
                return "Type 2";
            case THIRD_TYPE:
                return "Type 3";
        }

        return "";
    }
}
