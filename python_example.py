from __future__ import print_function


def get_smhi_data(customerid, subscriptionid, password, place, product,
                  typecase=None, date=None):
    """Minimal python function to get data from the API."""
    import requests
    import hashlib
    import time

    # Create the headers
    today = time.strftime("%Y-%m-%d")
    md5 = hashlib.md5()
    md5.update("{0}:{1}".format(today, password))
    key = "{0}:{1}:{2}".format(customerid, subscriptionid, md5.hexdigest())

    accept = "application/json"

    headers = {"key": key, "accept": accept}

    # Create the URL
    url = "http://fastighetsstyrning.smhi.se/ws/enloss/{0}/{1}".format(place,
                                                                       product)

    if typecase is not None:
        url = "/".join([url, typecase])

    if date is not None:
        url = "/".join([url, date])

    # Perform request to API
    response = requests.get(url, headers=headers)

    if response.status_code != 200:
        # Not successful or no content
        print(response.json())
        return None

    else:
        return response.json()
