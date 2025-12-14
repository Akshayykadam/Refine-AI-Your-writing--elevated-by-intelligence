import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { StyleSheet, Text, View, TouchableOpacity, Linking, Platform, useColorScheme, ScrollView, SafeAreaView } from 'react-native';

const PRIMARY_COLOR = '#15C39A';

export default function App() {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === 'dark';

  const styles = getStyles(isDark);

  const openSettings = () => {
    if (Platform.OS === 'android') {
      Linking.sendIntent('android.settings.ACCESSIBILITY_SETTINGS');
    }
  };

  const openGitHub = () => {
    Linking.openURL('https://github.com/Akshayykadam');
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style={isDark ? "light" : "dark"} />
      <ScrollView contentContainerStyle={styles.scrollContent}>

        {/* Header */}
        <View style={styles.header}>
          <Text style={styles.appTitle}>Refine.AI ✨</Text>
          <Text style={styles.tagline}>Elevate your writing, everywhere.</Text>
        </View>

        {/* Main Action Card */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Get Started</Text>
          <Text style={styles.cardDescription}>
            enable the Accessibility Service to let Refine.AI assist you in any app.
          </Text>

          <View style={styles.stepsContainer}>
            <View style={styles.stepRow}>
              <Text style={styles.stepNumber}>1</Text>
              <Text style={styles.stepText}>Tap the button below</Text>
            </View>
            <View style={styles.stepRow}>
              <Text style={styles.stepNumber}>2</Text>
              <Text style={styles.stepText}>Find "Refine.AI" in the list</Text>
            </View>
            <View style={styles.stepRow}>
              <Text style={styles.stepNumber}>3</Text>
              <Text style={styles.stepText}>Toggle it ON</Text>
            </View>
          </View>

          <TouchableOpacity style={styles.primaryButton} onPress={openSettings}>
            <Text style={styles.primaryButtonText}>Enable Service</Text>
          </TouchableOpacity>
        </View>

        {/* About Section */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>About</Text>
          <Text style={styles.cardDescription}>
            Refine.AI is a system-wide writing assistant powered by Gemini. It helps you rewrite, correct, and tone-switch your text directly within your favorite apps.
          </Text>
          <Text style={[styles.cardDescription, { marginTop: 10 }]}>
            Privacy First: Logic runs locally where possible, and text is only processed when you explicitly ask for it.
          </Text>
        </View>

        {/* Developer Section */}
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Developer</Text>
          <Text style={styles.cardDescription}>
            Built with ❤️ by Akshay Kadam.
          </Text>
          <TouchableOpacity style={styles.secondaryButton} onPress={openGitHub}>
            <Text style={styles.secondaryButtonText}>Visit My GitHub</Text>
          </TouchableOpacity>
        </View>

      </ScrollView>
    </SafeAreaView>
  );
}

const getStyles = (isDark: boolean) => StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: isDark ? '#121212' : '#F5F5F7',
  },
  scrollContent: {
    padding: 24,
    paddingTop: 60,
  },
  header: {
    marginBottom: 40,
    alignItems: 'center',
  },
  appTitle: {
    fontSize: 32,
    fontWeight: '800',
    color: isDark ? '#FFFFFF' : '#1C1C1E',
    marginBottom: 8,
    letterSpacing: 0.5,
  },
  tagline: {
    fontSize: 16,
    color: isDark ? '#A1A1A6' : '#86868B',
    fontWeight: '500',
  },
  card: {
    backgroundColor: isDark ? '#1C1C1E' : '#FFFFFF',
    borderRadius: 20,
    padding: 24,
    marginBottom: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: isDark ? 0.3 : 0.05,
    shadowRadius: 12,
    elevation: 4,
  },
  cardTitle: {
    fontSize: 20,
    fontWeight: '700',
    color: isDark ? '#FFFFFF' : '#1C1C1E',
    marginBottom: 12,
  },
  cardDescription: {
    fontSize: 15,
    color: isDark ? '#D1D1D6' : '#636366',
    lineHeight: 22,
  },
  stepsContainer: {
    marginTop: 16,
    marginBottom: 24,
  },
  stepRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  stepNumber: {
    width: 24,
    height: 24,
    borderRadius: 12,
    backgroundColor: isDark ? '#2C2C2E' : '#E5E5EA',
    color: PRIMARY_COLOR,
    textAlign: 'center',
    textAlignVertical: 'center', // Android
    lineHeight: 24, // iOS
    fontSize: 14,
    fontWeight: 'bold',
    marginRight: 12,
    overflow: 'hidden',
  },
  stepText: {
    fontSize: 15,
    color: isDark ? '#E5E5EA' : '#3A3A3C',
  },
  primaryButton: {
    backgroundColor: PRIMARY_COLOR,
    paddingVertical: 16,
    borderRadius: 14,
    alignItems: 'center',
    shadowColor: PRIMARY_COLOR,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 4,
  },
  primaryButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '700',
  },
  secondaryButton: {
    marginTop: 16,
    backgroundColor: isDark ? '#2C2C2E' : '#F2F2F7',
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
  },
  secondaryButtonText: {
    color: isDark ? '#FFFFFF' : '#1C1C1E',
    fontSize: 16,
    fontWeight: '600',
  },
});
