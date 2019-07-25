defaults appId: "AppCrm", trapVersion: MIBv2

entryLog(id: 10339L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Subscriber activate fail - Subscriber out of sync {0}"
    reason = "Failed to add the subscriber profile or services to Redknee ABM."

    addRecovery "Make sure Redknee ABM is up and running."

    addRecovery "Make sure the connection parameters to ABM are configured properly."
}

entryLog(id: 10340L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to UPS at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee ABM.  Either a CORBA client setting was not set up properly or ABM is not running."

    addRecovery "Make sure Redknee ABM is up and running."

    addRecovery "Make sure the connection parameters to ABM are configured properly."

    addClearedBy entryLog(id: 10341L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to UPS at {0}:{1}"
        reason = "A CORBA connection from E-Care CC to Redknee ABM has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 10342L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to ECP at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee ECP.  Either a CORBA client setting was not set up properly or ECP is not running."

    addRecovery "Make sure Redknee ECP is up and running."

    addRecovery "Make sure the connection parameters to ECP are configured properly."

    addClearedBy entryLog(id: 10343L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to ECP at {0}:{1}"
        reason = "A CORBA connection from E-Care CC to Redknee ECP has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 10344L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to OCG at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee OCG.  Either a CORBA client setting was not set up properly or OCG is not running."

    addRecovery "Make sure Redknee OCG is up and running."

    addRecovery "Make sure the connection parameters to OCG are configured properly."

    addClearedBy entryLog(id: 10345L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to OCG at {0}:{1}"
        reason = "A CORBA connection from E-Care CC to Redknee OCG has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 10347L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to SMSB at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee SMSB.  Either a CORBA client setting was not set up properly or SMSB is not running."

    addRecovery "Make sure Redknee SMSB is up and running."

    addRecovery "Make sure the connection parameters to SMSB are configured properly."

    addClearedBy entryLog(id: 10346L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to SMSB at {0}:{1}"
        reason = "A CORBA connection from E-Care CC to Redknee SMSB has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 10348L, severity: MINOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "SogClient executeCommand fail due to absence of connection - {0}, {1}"
    reason = "Failed to execute a command on the HLR because a client connection from E-Care CC to the HLR was not established."

    addRecovery "Establish a client connection from E-Care CC to the HLR."

    addClearedBy entryLog(id: 10397L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "SogClient connection re-established - {0}, {1}"
        reason = "A client connection from E-Care CC to the HLR gateway has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 10357L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Subscriber change state failure - unable to charge last month charge - lastMonthCharge = {0}, billCycleDay = {1}"
    reason = "E-Care CC failed to create the transaction for the given subscriber.  The last month charge could not be calculated."

    addRecovery "Make sure the transaction parameters are configured properly."
}

entryLog(id: 10358L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Subscriber create fail - unable to calculate first month charge - FirstMonthCharge = {0} and billCycleDay={1}"
    reason = "E-Care CC failed to create the transaction for the given subscriber.  The first month charge could not be calculated or the bill cycle day could not be found."

    addRecovery "Make sure the subscriber's bill cycle day is defined."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 10359L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Failed to execute command from client - response code {0}"
    reason = "E-Care CC failed to execute a command on the HLR.  Either the connection from E-Care CC to the HLR does not exist (or is down), or the command was invalid."

    addRecovery "Make sure the connection from E-Care CC to the HLR has been established."

    addRecovery "Make sure the HLR command issued by E-Care CC is valid."
}

entryLog(id: 10360L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "SmsProvisionAgent - fail to add subscriber - result code = {0}"
    reason = "E-Care CC failed to add the subscriber to Redknee SMSB.  The connection to SMSB may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to Redknee SMSB has been established."

    addRecovery "Make sure SMSB is up and running."

    addRecovery "Make sure the subscriber's MSISDN, HLR ID, and IMIS are valid."

    addRecovery "Make sure the parameters on the SMSB Client Configuration page in the GUI are valid."
}

entryLog(id: 10361L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "VoiceProvisionAgent - fail to add subcriber - result code = {0}"
    reason = "E-Care CC failed to add the subscriber to Redknee ECP.  The connection from E-Care CC to ECP may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to ECP has been established."

    addRecovery "Make sure ECP is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 10362L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Failed to instantiate SogClients based on items in SogPropertyHome"
    reason = "An internal software error occurred in E-Care CC.  E-Care CC failed to instantiate HLR clients.  Internal software parameters may not be configured properly."

    addRecovery "Minor problem requiring Redknee GTAC assistance. Please contact Redknee GTAC for support."
}

entryLog(id: 10387L, trapId: SOFTWARE_EXCEPTION) {
    message = "Account number rollover for service provider {0}"
    reason = "The last available number in the given service provider's account number pool has been reached.  The account numbers will be rolled over for reuse."

    addRecovery "No action required.  The account numbers will be rolled over for reuse."
}

entryLog(id: 10388L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "NFS Link is down: {0}"
    reason = "E-Care CC's NFS mount link to an external application (for example, Redknee SMSB or URS) is down."

    addRecovery "Make sure the connection from E-Care CC to the external application (for example, Redknee SMSB or URS) is established."

    addRecovery "Make sure the external application is up and running."

    addRecovery "Remount the event record directory of the external application."

    addClearedBy entryLog(id: 10389L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "NFS Link is Up: {0}"
        reason = "E-Care CC's NFS mount link to an external application (for example, Redknee SMSB or URS) has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 10504L, severity: MAJOR, trapId: UNEXPECTED_DATA) {
    message = "Invalid Roaming Entry"
    reason = "E-Care CC found an invalid entry in the roaming charges file.  The entry is not in the expected format and cannot be processed."

    addRecovery "Correct the roaming entry in the roaming charges file so that it follows the correct format."

    addRecovery "If necessary, contact Redknee GTAC for support."
}

entryLog(id: 10505L, severity: MAJOR, trapId: UNEXPECTED_DATA) {
    message = "Invalid roaming file."
    reason = "E-Care CC received an invalid roaming charges file.  The file cannot be processed.  The filename extension must be '.roam'."

    addRecovery "Provide a roaming charges file that has the correct format."

    addRecovery "Make sure the filename extension is '.roam'."

    addRecovery "If required, contact Redknee GTAC for support."
}

entryLog(id: 10531L, severity: MINOR, trapId: UNEXPECTED_DATA) {
    message = "Invalid TPS file {0}"
    reason = "E-Care CC received an invalid TPS payment file.  The file may be in the wrong format.  The file cannot be processed."

    addRecovery "Provide a TPS payment file that has the correct format."

    addRecovery "If required, contact Redknee GTAC for support."
}

entryLog(id: 10532L, severity: MINOR, trapId: UNEXPECTED_DATA) {
    message = "Invalid TPS entry"
    reason = "E-Care CC found an invalid entry in the TPS payment file.  The entry is not in the expected format and cannot be processed."

    addRecovery "Correct the TPS entry in the TPS payment file so that it follows the correct format."

    addRecovery "If required, contact Redknee GTAC for support."
}

entryLog(id: 10533L, severity: MINOR, trapId: DATABASE_ACCESS) {
    message = "Update Transaction Failure"
    reason = "A failure occurred while E-Care CC was attempting to record an entry in the transaction table in the database."

    addRecovery "Make sure the connection from E-Care CC to the database is established."

    addRecovery "Make sure the database is up and running."

    addRecovery "If required, contact Redknee GTAC for support."
}

entryLog(id: 10534L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "General payment processing failure. {0}"
    reason = "An error occurred while E-Care CC was processing a general payment."

    addRecovery "Check the log files and event record files to determine the detailed cause of failure."
}

entryLog(id: 10535L, severity: MINOR) {
    message = "UPS update fails for payment. "
    reason = "Unable to update UPS for payment "

    addRecovery "Make sure UPS is up"

    addRecovery "Check log/ER file to find  detailed reason"
}

entryLog(id: 10573L, severity: MINOR) {
    message = "Subscriber removal fail - Subscriber out of sync {0}"
    reason = "na"

    addRecovery "Check log/om file to find which server failed to remove this subscriber and its detailed reason"
}

entryLog(id: 10582L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Subscriber out of sync.  Dunning process failed to update subscriber {0} for account state {1}."
    reason = "E-Care CC's dunning process failed to update the given subscriber in the given account state."

    addRecovery "Minor problem requiring Redknee GTAC assistance.  Please contact Redknee GTAC for support."
}

entryLog(id: 10583L, severity: MAJOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Invoice generation for account {0} has failed."
    reason = "Invoice generation for the given account has failed."

    addRecovery "Major problem requiring Redknee GTAC assistance.  Please contact Redknee GTAC for support."
}

entryLog(id: 10609L, severity: MINOR, trapVersion: MIBv1, trapId: SOFTWARE_EXCEPTION) {
    message = "HLR Deactivation failed for subscriber %1."
    reason = "HLR failed while executing commands to deactivate a subscriber. "

    addRecovery "Check subscriber state. check HLR for the same subscriber. Fix subscriber data and try again."

    addRecovery "Contact Redknee GTAC"
}

entryLog(id: 10616L, severity: MINOR) {
    message = "No Destination Zone Prefix was found for the Destination MSISDN {0}"
    reason = "Destination Zone Prefix was not found for the specified Destination MSISDN"

    addRecovery "Provision the zone prefix in the E-Care CC GUI."
}

entryLog(id: 10617L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to Loyalty at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee Loyalty.  Either a CORBA client setting was not set up properly or Loyalty is not running."

    addRecovery "Make sure Redknee Loyalty is up and running."

    addRecovery "Make sure the connection parameters to Loyalty are configured properly."

    addClearedBy entryLog(id: 10618L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "COBRA connection established to Loyalty at {0}:{1}"
        reason = "A CORBA connection from E-Care CC to Redknee Loyalty has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 10892L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to IPCG at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee IPCG.  Either a CORBA client setting was not set up properly or IPCG is not running."

    addRecovery "Make sure Redknee IPCG is up and running."

    addRecovery "Make sure the connection parameters to IPCG are configured properly."

    addClearedBy entryLog(id: 10893L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to IPCG {0}:{1}"
        reason = "A CORBA client connection from E-Care CC to Redknee IPCG has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 10949L, trapId: CONFIGURATION_PARAMETER) {
    message = "Identifier Sequence has been rolled over for Sequence name: {0}."
    reason = "The given identifier sequence has reached its maximum value.  The sequence will be rolled over for reuse."

    addRecovery "No action required.  The sequence will be rolled over for reuse."
}

entryLog(id: 10950L, severity: MAJOR) {
    message = "Usage Report Task failure"
    reason = "Connection to database is down"

    addRecovery "check database is running"

    addRecovery "check connection to database is ok"
}

entryLog(id: 10962L) {
    message = "Subscriber with non-zero balance on a group pooled account"
    reason = ""

    addRecovery "Recalculate and update group and individual balances"
}

entryLog(id: 10996L, severity: MAJOR) {
    message = "Failed to delete auxiliary service {0}."
    reason = "Failed to delete specified auxiliary service "

    addRecovery "Verify that ECP is up."

    addRecovery "Verify network connectivity. "
}

entryLog(id: 11029L, severity: MINOR, trapId: UNEXPECTED_DATA) {
    message = "Ghost Subscriber <{0}> detected on SMSB "
    reason = """A "ghost" postpaid subscriber was detected in Redknee SMSB (as determined from SMSB's ER 311).  The subscriber is deactivated in Redknee E-Care CC and should be removed from SMSB."""

    addRecovery """Log in to Redknee SMSB and remove the "ghost" postpaid subscriber."""
}

entryLog(id: 11061L, severity: MAJOR, trapId: UNEXPECTED_DATA) {
    message = "Could not create call detail entry for Charged MSISDN {0}"
    reason = "E-Care CC was unable to create a call detail record for the given charged MSISDN.  No matching subscriber profile was found for the charged MSISDN in either ER 501 (from Redknee URS) or ER 511 (from Redknee IPCG)."

    addRecovery "Make sure the subscriber is provisioned in Redknee E-Care CC."
}

entryLog(id: 11062L, severity: MAJOR, trapVersion: MIBv1, trapId: UNEXPECTED_DATA) {
    message = "Could not create call detail entry for Charged MSISDN {0}"
    reason = "E-Care CC was unable to create a call detail record for the given charged MSISDN.  An invalid subscriber state was detected for the charged MSISDN in either ER 501 (from Redknee URS) or ER 511 (from Redknee IPCG)."

    addRecovery """Make sure the subscriber's state in E-Care CC is not "Suspended" or "Deactivated"."""
}

entryLog(id: 11379L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to ABM Bucket provisioning at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee ABM's bucket provisioning interface.  Either a CORBA client setting was not set up properly or ABM is not running."

    addRecovery "Make sure Redknee ABM is up and running."

    addRecovery "Make sure the connection parameters to ABM's bucket provisioning interface are configured properly."

    addClearedBy entryLog(id: 11380L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to ABM Bucket Provisioning {0}:{1}"
        reason = "A CORBA client connection from E-Care CC to Redknee ABM bucket provisioning interface has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 11772L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to IPCG Sub Provisioning at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee IPCG's subscriber provisionning interface.  Either a CORBA client setting was not set up properly or ICPG is not running."

    addRecovery "Make sure Redknee IPCG is up and running."

    addRecovery "Make sure the connection parameters to Redknee IPCG's subscriber provisioning interface are configured properly."

    addClearedBy entryLog(id: 11775L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA connection to IPCG Subscriber Provisioning at {0}:{1}"
        reason = "A CORBA client connection from E-Care CC to Redknee IPCG's subscriber provisioning interface has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 11773L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to IPCG Rating Provisioning at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee IPCG's rating provisionning interface.  Either a CORBA client setting was not set up properly or ICPG is not running."

    addRecovery "Make sure Redknee IPCG is up and running."

    addRecovery "Make sure the connection parameters to Redknee IPCG's rating provisioning interface are configured properly."

    addClearedBy entryLog(id: 11774L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA connection to IPCG Rating Provisioning at {0}:{1}"
        reason = "A CORBA client connection from E-Care CC to Redknee IPCG's rating provisioning interface has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 12654L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Connection to  MobileOfficeManager server lost"
    reason = "The connection between E-Care CC and Redknee MOM was lost."

    addRecovery "Make sure the CORBA name server is up and running."

    addRecovery "Make sure Redknee MOM is up and running."

    addClearedBy entryLog(id: 12655L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Connection to MOM interface restored"
        reason = "The connection between E-Care CC and Redknee MOM has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 12656L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to ECP Rating Provisioning at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee ECP's rating provisionning interface.  Either a CORBA client setting was not set up properly or ECP is not running."

    addRecovery "Make sure Redknee ECP is up and running."

    addRecovery "Make sure the connection parameters to Redknee ECP's rating provisioning interface are configured properly."

    addClearedBy entryLog(id: 12657L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA connection to ECP Rating Provisioning at {0}:{1}"
        reason = "A CORBA client connection from E-Care CC to Redknee ECP's rating provisioning interface has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 12669L, severity: MINOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Connection to ECP down when trying to provision vpndiscount"
    reason = "E-Care CC could not provision a VPN discount to a subscriber because the connection to Redknee ECP was down."

    addRecovery "Make sure Redknee ECP is up and running."

    addRecovery "Make sure the connection parameters to ECP are configured properly."

    addClearedBy entryLog(id: 12670L, severity: MINOR, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Connection to Ecp restored  to provision Vpndiscount"
        reason = "The connection between E-Care CC and Redknee ECP has been (re)established.  E-Care CC is able to provision VPN discounts to subscribers again."

        addRecovery "No action required."
    }
}

entryLog(id: 12719L, severity: MINOR, trapId: CLIENT_CONNECTION_STATE_CHANGE) {
    message = "Connection to ECP down when trying to querying for RatePlans at {0}:{1}"
    reason = "E-Care CC could not query Redknee ECP for rate plans because the connection to ECP was down."

    addRecovery "Make sure Redknee ECP is up and running."

    addRecovery "Make sure the connection parameters to ECP are configured properly."

    addClearedBy entryLog(id: 12720L, severity: MINOR, trapId: CLIENT_CONNECTION_STATE_CHANGE) {
        message = "Connection to Ecp restored  to query rateplans at {0}:{1}"
        reason = "The connection between E-Care CC and Redknee ECP has been (re)established.  E-Care CC is able to query ECP for rate plans again."

        addRecovery "No action required."
    }
}

entryLog(id: 12742L, severity: MINOR) {
    message = "Extracted call details have not been rerated or received from ECP in configured time interval"
    reason = "During the rerating process, call details which have been extracted from the call detail tables have not been matched up to a corresponding call detail from ECP in the alloted time frame."

    addRecovery "Minor problem requiring Redknee GTAC assistance.  Please contact Redknee GTAC for support."

    addRecovery "Query the rerating call detail tables for unreceived call details and their extract date to determine a list of call details that needs attention."
}

entryLog(id: 12865L, severity: MAJOR, trapVersion: MIBv1, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to Pin Manager at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to the PIN Manager module.  Either a CORBA client setting was not set up properly or the PIN Manager module is not running."

    addRecovery "Make sure the PIN Manager module is up and running."

    addRecovery "Make sure the connection parameters to the PIN Manager module are configured properly."

    addClearedBy entryLog(id: 12866L, trapVersion: MIBv1, trapId: EXTERNAL_LINK_UP) {
        message = "Establish CORBA client connection to Pin Manager at {0}:{1}"
        reason = "A CORBA connection from E-Care CC to the PIN Manager module has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 12901L, severity: MAJOR) {
    message = "Failed to provision Subscriber[ID={0}, MSISDN={1}] to AAA Service[ID={2}, Name={3}]."
    reason = "The AAA client interface raised an exception."
}

entryLog(id: 12924L, severity: MAJOR) {
    message = "AAA Profile Not Updated for Subscriber[ID={0}, MSISDN={1}]."
    reason = "An exception occurred during CRM Subscriber profile update which prevented the AAA profile update from being attempted."

    addRecovery "Manually compare the Subscriber profile in the external AAA service with the CRM Subscriber profile."
}

entryLog(id: 12925L, severity: MAJOR) {
    message = "AAA Profile Update Failure for Subscriber[ID={0}, MSISDN={1}]."
    reason = "The AAA Client  raised an exception when called after a subscriber update."

    addRecovery "Inspect the application logs for details related to the exception."

    addRecovery "Manually verify that the CRM Subscriber profile matches the AAA Subsciber profile."
}

entryLog(id: 13147L, severity: CRIT, trapId: SOFTWARE_EXCEPTION) {
    message = "SMSB ER Indices Configuration could not be found.Polling of SMSB ER(311) will not be successfull."
    reason = "The SMSB ER Indices Configuration could not be found or is not filled out in the GUI.  The polling of ER 311 from Redknee's SMSB application will fail."

    addRecovery "Make sure the SMSB ER Indices Configuration is correctly filled out in the GUI so that the poller can execute properly."
}

entryLog(id: 13188L, severity: MAJOR, trapId: RESOURCE_UTILIZATION) {
    message = "Available Msisdns for the Mobile Number Group [{0}] has fallen below the minimum allowed for this Group"
    reason = """The number of "Available" state MSISDNs in the given Mobile Number Group has fallen below the minimum required for the group."""

    addRecovery """Add more "Available" state MSISDNs to the given Mobile Number Group."""

    addRecovery """Alternatively, lower the minimum threshold of "Available" state MSISDNs required for the group."""

    addClearedBy entryLog(id: 13189L, trapId: RESOURCE_UTILIZATION) {
        message = "Available Msisdns for the Mobile Number Group [{0}] are above the minimum allowed for this Group"
        reason = """The number of "Available" state MSISDNs in the given Mobile Number Group is once again above the minimum required for the group."""

        addRecovery "No action required."
    }
}

entryLog(id: 13214L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish RMI client connection to the Invoice Server {0}:{1}"
    reason = "Unable to establish an RMI client connection from CRM to the Invoice Server.  Either the RMI client setting was not set up properly or the Invoice Server is not running."

    addRecovery "Make sure the Invoice Server is up and running."

    addRecovery "Make sure the connection parameters to the Invoice Server are configured properly."

    addClearedBy entryLog(id: 13215L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish RMI client connection to the Invoice Server {0}:{1}"
        reason = "An RMI connection from CRM to the Invoice Server has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 13666L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to Balance Management Service"
    reason = "Unable to establish a CORBA client connection to Balance Management Service.  Either a CORBA client setting was not set up properly or the external application is not running or unreachable."

    addRecovery "Make sure the external application is up and running."

    addRecovery "Make sure the connection parameters to the external application are configured properly."

    addClearedBy entryLog(id: 13667L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to Balance Management Service"
        reason = "A CORBA connection to Balance Management Service has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 13705L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA connection to Bearer Service at {0}:{1}"
    reason = "Unable to establish a CORBA client connection from E-Care CC to Redknee ECP Bearer Service.  Either a CORBA client setting was not set up properly or ECP Bearer Service is not running."

    addRecovery "Make sure Redknee ECP is up and running."

    addRecovery "Make sure the connection parameters to ECP are configured properly."

    addClearedBy entryLog(id: 13706L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to ECP Bearer Service {0}:{1}"
        reason = "A CORBA connection from E-Care CC to Redknee ECP Bearer Service has been (re)established."

        addRecovery "No action required."
    }
}

entryLog(id: 13779L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "IPCProvisionAgent - fail to add subscriber - result code = {0}"
    reason = "E-Care CC failed to add the subscriber to Redknee IPRC.  The connection from E-Care CC to IPRC may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to Redknee IPRC has been established."

    addRecovery "Make sure IPRC is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 13780L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "AlcatelProvisionAgent - fail to add subscriber and service package - result code = {0}"
    reason = "E-Care CC failed to add the subscriber to Alcatel SSC.  The connection from E-Care CC to Alcatel SSC may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to Alcatel SSC has been established."

    addRecovery "Make sure Alcatel SSC is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 13781L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "VoiceMailServer (VoicemailProvisionAgent) provision - fail to add subcriber - result code = {0}"
    reason = "E-Care CC failed to add the subscriber to the VoiceMail Server.  The connection from E-Care CC to VoiceMail Server may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to the VoiceMail Server has been established."

    addRecovery "Make sure the VoiceMail Server is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 13782L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "AlcatelUnprovisionAgent - fail to remove service from Alcatel SSC - result code = {0}"
    reason = "E-Care CC failed to remove the subscriber service from Alcatel SSC.  The connection from E-Care CC to Alcatel SSC may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to Alcatel SSC has been established."

    addRecovery "Make sure Alcatel SSC is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."

    addRecovery "Make sure the correct blocked service package is configured in CRM."
}

entryLog(id: 13783L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "VoiceUnprovisionAgent - fail to delete subscriber - result code = {0}"
    reason = "E-Care CC failed to remove the subscriber from Redknee ECP.  The connection from E-Care CC to ECP may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to ECP has been established."

    addRecovery "Make sure ECP is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 13784L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "SmsUnprovisionAgent - fail to delete subscriber - result code = {0}"
    reason = "E-Care CC failed to remove the subscriber from Redknee SMSB.  The connection to SMSB may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to Redknee SMSB has been established."

    addRecovery "Make sure SMSB is up and running."

    addRecovery "Make sure the subscriber's MSISDN, HLR ID, and IMIS are valid."

    addRecovery "Make sure the parameters on the SMSB Client Configuration page in the GUI are valid."
}

entryLog(id: 13785L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "IPCUnprovisionAgent - fail to remove subscriber - result code = {0}"
    reason = "E-Care CC failed to remove the subscriber from Redknee IPRC.  The connection from E-Care CC to IPRC may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to Redknee IPRC has been established."

    addRecovery "Make sure IPRC is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 13786L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "VoiceMailServer (VoicemailUnprovisionAgent) unprovision - fail to unprovision subcriber service - result code = {0}"
    reason = "E-Care CC failed to unprovision the subscriber service from the VoiceMail Server.  The connection from E-Care CC to VoiceMail Server may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to the VoiceMail Server has been established."

    addRecovery "Make sure the VoiceMail Server is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 13787L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Alcatel Suspend Agent - fail to suspend service from Alcatel SSC - result code = {0}"
    reason = "E-Care CC failed to suspend the subscriber service in Alcatel SSC.  The connection from E-Care CC to Alcatel SSC may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to Alcatel SSC has been established."

    addRecovery "Make sure Alcatel SSC is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."

    addRecovery "Make sure the correct blocked service package is configured in CRM."
}

entryLog(id: 13788L, severity: MINOR, trapId: SOFTWARE_EXCEPTION) {
    message = "Alcatel Resume Agent - fail to resume service from Alcatel SSC - result code = {0}"
    reason = "E-Care CC failed to resume the subscriber service in Alcatel SSC.  The connection from E-Care CC to Alcatel SSC may be down, or the subscriber's information was not configured properly."

    addRecovery "Make sure the connection from E-Care CC to Alcatel SSC has been established."

    addRecovery "Make sure Alcatel SSC is up and running."

    addRecovery "Make sure the subscriber's information is configured properly."
}

entryLog(id: 15026L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA client connection to URCS ScreeningTemplates at {0}:{1}"
    reason = "Unable to establish a CORBA connection from CRM to Redknee URCS Screening Template CORBA. Either a CORBA client setting was not set up properly or URCS/CPS are not running."

    addRecovery "Make sure Redknee URCS and CPS are up and running."

    addRecovery "Make sure the connection parameters to URCS are configured properly."

    addClearedBy entryLog(id: 15025L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to URCS ScreeningTemplates at {0}:{1}"
        reason = "A CORBA connection from CRM to Redknee URCS Screening Template CORBA interface has been (re)established."
    }
}

entryLog(id: 15027L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA client connection to URCS BundleProfileProvision at {0}:{1}"
    reason = "Unable to establish a CORBA connection from CRM to Redknee URCS BundleProfileProvision CORBA. Either a CORBA client setting was not set up properly or URCS/CPS are not running."

    addRecovery "Make sure Redknee URCS and CPS are up and running."

    addRecovery "Make sure the connection parameters to URCS are configured properly."

    addClearedBy entryLog(id: 15028L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to URCS BundleProfileProvision at {0}:{1}"
        reason = "A CORBA connection from CRM to Redknee URCS Bundle Profile Provision CORBA interface has been (re)established."
    }
}

entryLog(id: 15029L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA client connection to URCS CategoryProvision at {0}:{1}"
    reason = "Unable to establish a CORBA connection from CRM to Redknee URCS CategoryProvision CORBA. Either a CORBA client setting was not set up properly or URCS/CPS are not running."

    addRecovery "Make sure Redknee URCS and CPS are up and running."

    addRecovery "Make sure the connection parameters to URCS are configured properly."

    addClearedBy entryLog(id: 15030L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to URCS CategoryProvision at {0}:{1}"
        reason = "A CORBA connection from CRM to Redknee URCS Category Provision CORBA interface has been (re)established."
    }
}

entryLog(id: 15031L, severity: MAJOR, trapId: EXTERNAL_LINK_CHANGE) {
    message = "Unable to establish CORBA client connection to URCS SubscriberBucketProvision at {0}:{1}"
    reason = "Unable to establish a CORBA connection from CRM to Redknee URCS SubscriberBucketProvision CORBA. Either a CORBA client setting was not set up properly or URCS/CPS are not running."

    addRecovery "Make sure Redknee URCS and CPS are up and running."

    addRecovery "Make sure the connection parameters to URCS are configured properly."

    addClearedBy entryLog(id: 15032L, trapId: EXTERNAL_LINK_CHANGE) {
        message = "Establish CORBA client connection to URCS SubscriberBucketProvision at {0}:{1}"
        reason = "A CORBA connection from CRM to Redknee URCS Subscriber Bucket Provision CORBA interface has been (re)established."
    }
}

entryLog(id: 15113L, severity: MAJOR, trapId: CONFIGURATION_PARAMETER) {
    message = "Configuration missing - {0}"
    reason = "Missing configuration."
}

entryLog(id: 15153L, severity: MAJOR, trapId: RESOURCE_UTILIZATION) {
    message = "The previous Expired Priceplan Services Clean Up Task has been running for more than an hour"
    reason = "The previous Expired Priceplan Services Clean Up Task has been running for more than an hour"

    addRecovery "No action required."

    addClearedBy entryLog(id: 15154L, trapId: RESOURCE_UTILIZATION) {
        message = "The previous Expired Priceplan Services Clean Up Task has been running for more than an hour"
        reason = "The previous Expired Priceplan Services Clean Up Task has been running for more than an hour"

        addRecovery "No action required."
    }
}

entryLog(id: 15159L, severity: MAJOR, trapId: EXTERNAL_ELEMENT) {
    message = "Failed to execute command for Subscriber[MSISDN={0}] . Service[ID={1}, NAME={2}, COMMAND TYPE={3}, COMMAND={4}, ERRORCODE={5}, RESPONSE={6}]."
    reason = "The provisioning client raised an exception"
}

// TODO: This clearing declaration appears orphaned -- need to check!
entryLog(id: 15009L, trapId: PROCESS_STATE_CHANGED) {
    message = "IPCG Data Aggregation Task Complete Successfully"
    reason = "Clearing the 15010 trap"

    addRecovery "No Action"

    clearsId = 15010L
}

// TODO: This clearing declaration appears orphaned -- need to check!
entryLog(id: 15011L, trapId: PROCESS_STATE_CHANGED) {
    message = "IPCG Data Aggregation Task Complete Successfully"
    reason = "Clearing the 15010 trap"

    clearsId = 15010L
}