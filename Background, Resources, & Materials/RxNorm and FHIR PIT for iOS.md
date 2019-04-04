# Accessing FHIR PIT and RxNorm APIs from an iOS Project

### Step 1: Install CocoaPods
We will rely on some third-party libraries to achieve HTTP networking from our iOS project. To do so, we must first set up our project to work with **CocoaPods**. Detailed installation instructions can be found [here](https://cocoapods.org "here").

Before we can add any pods to our project, we must first install **CocoaPods**, if not already installed. To install, open a new terminal window and run the command:

    sudo gem install cocoapods

You will notice **CocoaPods** gets installed. Next, we need to add some pods to our project. This can easily be achieved by creating a `Podfile` which will reside in your project directory. The structure of the `Podfile` looks like:

    platform :ios, '11.0'
    use_frameworks!

    target 'MyApp' do
        // pods we wish to install will be listed here
    end

Then, use a terminal window to navigate to your project directory and run the following command:

    $ pod install

This will run the cocoapods installation on your project, and will create some new files in your project directory. Most notably, you will have a new file `{Your Project Name}.xcworkspace`. Use this file to work on your project from now on, instead of `{Your Project Name}.xcodeproj`.

### Step 2: Install Alamofire and SwiftyJSON
**Alamofire** is a project that simplifies the HTTP networking process inside iOS projects. To install to our project, we can follow their instructions found [here](https://github.com/Alamofire/Alamofire#installation "here").

Simply, we can modify our `Podfile` so it contains the following:

    platform :ios, '11.0'
    use_frameworks!

    target 'MyApp' do
        pod 'Alamofire'
    end

**SwiftyJSON** is a project that offers better and easier JSON serialization than what is found in the default Apple API. To install to our project, we can follow their instructions found [here](https://github.com/SwiftyJSON/SwiftyJSON#integration "here").

Similar to how we modified our `Podfile` to include **Alamofire**, we can add the following to include **SwiftyJSON**:

    pod 'SwiftyJSON'

After we have added those two pods to our podfile, we should open our terminal window again and navigate back to our project directory. Again, we will run this command:

    $ pod install

This will install our two pods to our project and they will now be usable.

### Example 1: Retrieving RXCUI from RxNorm for Medication Name
We will now walk through making a call to RxNorm with a medication name in order to acquire the RXCUI. First, in order to use **Alamofire** and **SwiftyJSON** in our class, we must import them:

    import Alamofire
    import SwiftyJSON

We will use the following url to query RxNorm for the resource that we desire. A more in depth and complete description of all the resources offered by the RxNorm and other APIs can be found [here](https://mor.nlm.nih.gov/download/rxnav/RxNormAPIREST.html#label:functions "here").

    https://rxnav.nlm.nih.gov/REST/rxcui.json?={medication_name}

We will insert our desired medication name inside the {}. So we can declare a let constant to hold this URL, making it easier to pass around.

    let urlForRxcui = "https://rxnav.nlm.nih.gov/REST/rxcui.json?=morphine"

To obtain our desired RXCUI from RxNorm, we will use **Alamofire** to form the request and **SwiftyJSON** to deserialize the result, as shown below:

    Alamofire.request(urlForRxcui, method: .get).responseJSON { payload in
        switch payload.result {
        case .success(let value):
            let json = JSON(value)
            print(json)
        case .failure(let error):
            print(error.localizedDescription)
        }
    }

The call that we make to `JSON(value)` calls a **SwiftyJSON** function to deserialize the received raw JSON to a more usable `[String: Any]` which can be easily used to extract the values that we are looking for.

### Example 2: Retrieving a Patient object from FHIR print
Using similar techniques as shown above, we can retrieve patient records from FHIR PIT. To search for a given patient, we will use the following URL:

    https://fire-pit.mihin.org/pit-ct1/baseDstu3/Patient?given={first}&family={last}

Replace {first} and {last} with the first name and last name for the patient.

    let urlForPatient = "https://fire-pit.mihin.org/pit-ct1/baseDstu3/Patient?given=john&family=smith"

Then, we formulate our request:

    Alamofire.request(urlForPatient, method: .get).authenticate(user: username, password: password).responseJSON { payload in
        switch payload.result {
        case .success(let value):
            let json = JSON(value)
            print(json)
        case .failure(let error):
            print(error.localizedDescription)
        }
    }

Note the `.authenticate(user:password:)` method chained onto our request. Use this to pass in your username and password for FHIR PIT, since unlike RxNorm, this API requires authentication. We can handle the incoming raw JSON the same way that we do in the first example by making a call to `JSON(value)` from **SwiftyJSON**.
