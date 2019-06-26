package io.zhenik.example.kafka.metrics.app;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import io.prometheus.client.SummaryMetricFamily;
import io.prometheus.client.hotspot.StandardExports;
import org.apache.kafka.common.metrics.JmxReporter;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.io.InvalidObjectException;
import java.lang.management.ManagementFactory;
import java.util.*;

public class CustomProducerCollector extends Collector {
    private static final Logger log = LoggerFactory.getLogger(JmxReporter.class);
    private String clientId;
    private String prefix;
    private MBeanServer platformMbeanServerProvider;

    public CustomProducerCollector(String clientId) {
        this.clientId = clientId;
        this.platformMbeanServerProvider = ManagementFactory.getPlatformMBeanServer();

        System.out.println(platformMbeanServerProvider.getMBeanCount());
        System.out.println(platformMbeanServerProvider.getDefaultDomain());
        System.out.println(Arrays.asList(platformMbeanServerProvider.getDomains()));
        System.out.println(Arrays.asList(platformMbeanServerProvider.getDomains()));

    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        GaugeMetricFamily recordSendTotal = new GaugeMetricFamily(
                "record-send-total",
                "OMG",
                getMBeanAttributeValue("producer-metrics", "record-send-total", Double.class));
        mfs.add(recordSendTotal);

        return mfs;
    }


    @SuppressWarnings("unchecked")
    public <T extends Number> T getMBeanAttributeValue(final String mBeanObjectName, final String attribute, final Class<T> returnType) {
        ObjectName objectName = getObjectNameFromString(mBeanObjectName);
        if (objectName == null) {
            // This also indicates that the mbeanObject is not registered.
            // Check to see if it is an exceptions-$class object
            if (mBeanObjectName.startsWith("exceptions")) {
                // The exceptions object is not added to the MBeanServer, as there
                // were no HTTP client exceptions raised yet. Return 0.
                if (returnType.equals(Double.class)) {
                    return (T) Double.valueOf(0d);
                } else if (returnType.equals(Long.class)) {
                    return (T) Long.valueOf(0L);
                }
            }
            String message = "Requested MBean Object not found";
            throw new IllegalArgumentException(message);
//            MBeanServerOperationException mBeanServerOperationException = new MBeanServerOperationException(message, new InvalidObjectException(message));
//            mBeanServerOperationException.addContextValue("objectName", mBeanObjectName);
//            mBeanServerOperationException.addContextValue("attribute", attribute);
//            mBeanServerOperationException.addContextValue("mbeanServerDomain", MBEAN_DOMAIN);
//            mBeanServerOperationException.addContextValue("mbeanObjectKey", MBEAN_OBJECT_KEY);
//            throw mBeanServerOperationException;
        }
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Object value;
        try {
            value = mBeanServer.getAttribute(objectName, attribute);
            final Number number;

            if (value instanceof Number) {
                number = (Number) value;
            } else {
                try {
                    number = Double.parseDouble(value.toString());
                } catch (NumberFormatException e) {
                    String message = "Failed to parse attribute value to number";

//                    MBeanServerOperationException mBeanServerOperationException = new MBeanServerOperationException(message, e);
//                    mBeanServerOperationException.addContextValue("objectName", objectName.getCanonicalName());
//                    mBeanServerOperationException.addContextValue("attribute", attribute);
//                    mBeanServerOperationException.addContextValue("mbeanServerDomain", MBEAN_DOMAIN);
//                    mBeanServerOperationException.addContextValue("mbeanObjectKey", MBEAN_OBJECT_KEY);
//                    mBeanServerOperationException.addContextValue("expectedAttributeReturnType", returnType.getCanonicalName());
//                    mBeanServerOperationException.addContextValue("resultAttributeValueReturnType", value != null ? value.getClass() : null);
//                    mBeanServerOperationException.addContextValue("resultAttributeValue", value);
//                    throw mBeanServerOperationException;
                    throw new IllegalArgumentException(message);
                }
            }

            if (returnType.equals(number.getClass())) {
                return (T)number;
            } else if (returnType.equals(Short.class)) {
                return (T)Short.valueOf(number.shortValue());
            } else if (returnType.equals(Integer.class)) {
                return (T)Integer.valueOf(number.intValue());
            } else if (returnType.equals(Long.class)) {
                return (T)Long.valueOf(number.longValue());
            } else if (returnType.equals(Float.class)) {
                return (T)Float.valueOf(number.floatValue());
            } else if (returnType.equals(Double.class)) {
                return (T)Double.valueOf(number.doubleValue());
            } else if (returnType.equals(Byte.class)) {
                return (T)Byte.valueOf(number.byteValue());
            }
        } catch (AttributeNotFoundException | InstanceNotFoundException | ReflectionException | MBeanException e) {
            String message;
            if (e instanceof AttributeNotFoundException) {
                message = "The specified attribute does not exist or cannot be retrieved";
            } else if (e instanceof  InstanceNotFoundException) {
                message = "The specified MBean does not exist in the repository.";
            } else if (e instanceof MBeanException) {
                message = "Failed to retrieve the attribute from the MBean Server.";
            } else {
                message = "The requested operation is not supported by the MBean Server ";
            }
//            MBeanServerOperationException mBeanServerOperationException = new MBeanServerOperationException(message, e);
//            mBeanServerOperationException.addContextValue("objectName", objectName.getCanonicalName());
//            mBeanServerOperationException.addContextValue("attribute", attribute);
//            mBeanServerOperationException.addContextValue("mbeanServerDomain", MBEAN_DOMAIN);
//            mBeanServerOperationException.addContextValue("mbeanObjectKey", MBEAN_OBJECT_KEY);
//            throw mBeanServerOperationException;
            throw new IllegalArgumentException(message);
        }
        return null;
    }

    public boolean validateMbeanObject(final String objectName) {
        ObjectName mbeanObject = getObjectNameFromString(objectName);
        if (mbeanObject == null) {
            String message = "Requested mbean object is not registered with the Platform MBean Server";
//            MBeanServerOperationException mBeanServerOperationException = new MBeanServerOperationException(message, new ValidationException(message));
//            mBeanServerOperationException.addContextValue("objectName", objectName);
//            mBeanServerOperationException.addContextValue("mbeanServerDomain", MBEAN_DOMAIN);
//            mBeanServerOperationException.addContextValue("mbeanObjectKey", MBEAN_OBJECT_KEY);
            throw new IllegalArgumentException(message);
        }
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        return mBeanServer.isRegistered(mbeanObject);
    }


    private ObjectName getObjectNameFromString(final String objectName) {
//        String objectNameWithDomain = "kafka.producer" + ":" + "type" + "=" + objectName + ",*";
//        String objectNameWithDomain = "kafka.producer" + ":" + "type" + "=" + "producer-metrics" + ",client-id="+clientId;
        String objectNameWithDomain = "kafka.producer" + ":" + "type" + "=" + objectName + ",client-id="+clientId;
        MBeanServer mBeanServer = platformMbeanServerProvider;
        ObjectName responseObjectName = null;
        try {
            ObjectName mbeanObjectName = new ObjectName(objectNameWithDomain);
            Set<ObjectName> objectNames = mBeanServer.queryNames(mbeanObjectName, null);
            for (ObjectName object: objectNames) {
                responseObjectName = object;
            }
        } catch (MalformedObjectNameException mfe) {
//            String message = String.format("Error in creating mbean object name from the string %s", objectName);
//            MBeanServerOperationException mBeanServerOperationException = new MBeanServerOperationException(message, mfe);
//            mBeanServerOperationException.addContextValue("objectName", objectName);
//            mBeanServerOperationException.addContextValue("mbeanServerDomain", MBEAN_DOMAIN);
//            mBeanServerOperationException.addContextValue("mbeanObjectKey", MBEAN_OBJECT_KEY);
            throw new IllegalArgumentException();
        }
        return responseObjectName;
    }


}
